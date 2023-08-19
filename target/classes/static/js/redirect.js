function getProductList() {
	    	var token = document.getElementById('accessToken').value;
	    	var userId = document.getElementById('sellerId').value;
	    	var userType = document.getElementById('userType').value.toLowerCase();
	        $.ajax({
                url: "/" + userType + "/" + userId,
                headers: {
                    'Authorization': token
                },
                success: function(xhr) {
                }
            });
 }