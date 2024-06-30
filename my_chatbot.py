from flask import Flask, request, jsonify
import pandas as pd
import time
import sys

app = Flask(__name__)

def slow_print(text, delay=0.03):
    for char in text:
        sys.stdout.write(char)
        sys.stdout.flush()
        time.sleep(delay)
    print()

class OpenAIBot:
    def __init__(self):
        self.user_info = {}
        self.stage = 0
        self.context_data = pd.DataFrame({
            'sleep_hours': [5],
            'stress_level': [8],
            'radiation_exposure': [120],
            'location': ['Mannheim']
        })

    def generate_response(self, prompt):
        if self.stage == 0:
            self.stage += 1
            return "Hello! How can I assist you today?"
        elif self.stage == 1:
            self.stage += 1
            return "Would you like me to suggest ways to care for your blemish-prone skin?"
        elif self.stage == 2:
            self.stage += 1
            return "Please select your skin tone: dark, light, medium, fair"
        elif self.stage == 3:
            self.user_info["skin_tone"] = prompt.lower()
            self.stage += 1
            return "What is your age?"
        elif self.stage == 4:
            self.user_info["age"] = prompt
            self.stage += 1
            return "Please select your race: White, African American, Asian, Latina, Other"
        elif self.stage == 5:
            self.user_info["race"] = prompt.lower()
            self.stage += 1
            context_analysis = self.analyze_context_data()
            product_recommendations = self.recommend_products()
            response = f"{context_analysis}\n\n{product_recommendations}\n\nWould you like to add these products to your Amazon cart or find local shops to buy from?"
            return response
        elif self.stage == 6:
            self.stage += 1
            return "Products added to your cart. If you order them today before 5pm you can get them delivered tomorrow.\n\nWould you like to add this to your calendar?"
        elif self.stage == 7:
            self.stage += 1
            return "Adding a reminder to order products and check progress in a few days.\n\nAnything else I can help you with?"
        else:
            return "Take care love!"

    def analyze_context_data(self):
        context = self.context_data.iloc[0]
        return f"It seems you have slept very few hours the last few days ({context['sleep_hours']} hours) and your stress levels are higher than normal (level {context['stress_level']}). There were also high radiation levels in {context['location']} ({context['radiation_exposure']} units)."

    def recommend_products(self):
        products = [
            "Nivea Purifying Cleanser (https://amazon.com/nivea-purifying-cleanser), (https://www.nivea.de/produkte/derma-skin-clear-waschgel-40059009738870001.html)",
            "Nivea Oil-Free Acne Fighting Moisturizer (https://amazon.com/nivea-oil-free-moisturizer), (https://www.nivea.de/produkte/cellular-luminous630-anti-pigmentflecken-pickelmale-serum-40060000180410001.html)",
            "Nivea Vitamin C Serum (https://amazon.com/nivea-vitamin-c-serum), (https://www.nivea.de/produkte/cellular-professional-serum-vitamin-c-40059009147120001.html)",
            "Nivea Sun Protect SPF 50 (https://amazon.com/nivea-sun-protect-spf-50), (https://www.nivea.de/produkte/schutz-and-pflege-sonnenspray-40058088566950001.html)"
        ]
        recommendations = "Based on your user information and context data, here are the recommended Nivea products for you:\n"
        recommendations += "\n".join(products)
        recommendations += "\n\nUsage instructions:\n"
        recommendations += "- Cleanse twice daily with Nivea Purifying Cleanser\n"
        recommendations += "- Apply Nivea Oil-Free Acne Fighting Moisturizer after cleansing\n"
        recommendations += "- Use Nivea Vitamin C Serum before moisturizing to prevent hyperpigmentation\n"
        recommendations += "- Apply Nivea Sun Protect SPF 50 as the last step of your morning routine"
        return recommendations

bot = OpenAIBot()

@app.route('/chat', methods=['POST'])
def chat():
    data = request.get_json()
    user_input = data.get("message")
    response = bot.generate_response(user_input)
    return jsonify({"response": response})

if __name__ == '__main__':
    app.run(debug=True)
