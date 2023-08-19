function validateBidAmount() {
//    var minBidAmountInput = document.getElementById("minBidAmount");
//    var minBidAmount = parseInt(minBidAmountInput.value);
//    var productMinBidAmount = parseInt('${productData.get().getMinBidAmount()}');
     var minBidAmountInput = document.getElementById("minBidAmount");
     var minBidAmount = parseInt(minBidAmountInput.value);
     var productMinBidAmount = parseInt(document.getElementById("sellerBidAmount").getAttribute("value"));

    if (minBidAmount <= productMinBidAmount) {
        alert("Bid amount must be greater than the minimum bid amount.");
        return false; // Prevent form submission
    }

    return true; // Allow form submission
}