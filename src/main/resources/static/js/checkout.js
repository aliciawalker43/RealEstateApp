//Stripe Checkout Integration	

// This is your test publishable API key.


async function initializePayment(amount) {
  const response = await fetch("/create-payment-intent", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ amount: amount }) // amount in cents
  });
  const { clientSecret } = await response.json();
  const elements = stripe.elements({ clientSecret });

  const addressElement = elements.create("address", { mode: "billing" });
  addressElement.mount("#billing-address-element");
  
  const paymentElement = elements.create("payment");
  paymentElement.mount("#payment-element");

  document.querySelector("#payment-form").addEventListener("submit", async (e) => {
    e.preventDefault();
    const { error } = await stripe.confirmPayment({
      elements,
      confirmParams: {
        return_url: "http://localhost:8080/complete.html",
      },
    });
    if (error) {
		 document.querySelector("#error-message").textContent = error.message;
		 } else if (paymentIntent && paymentIntent.status === "succeeded") {
		   document.querySelector("#success-message").textContent = "Payment successful!";
}
  });
}

// Example usage: Initialize payment with an amount of $20.00 (2000 cents)
initializePayment();
	