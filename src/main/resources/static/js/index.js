//Handling the ids
let userId = getUrlParameter('userId');
if(userId === null || userId === '') {
	userId = localStorage.getItem('userId');
	
	if(userId === null || userId === '') {
		document.getElementById('createUser').value = true;
	} else {
		/*fetch('/savedPreferences?userId='+userId)
			.then(response => response.json())
			.then(jsonResponse => {
				console.log(jsonResponse);
			});*/
		window.location.href = '/?userId=' + userId;
	}
	
}

if(userId !== null && userId !== '') {
	localStorage.setItem('userId', userId);
}



//Handling the buttons
let marsApiButtons = document.querySelectorAll("button[id*='marsApi']")

marsApiButtons.forEach(button => button.addEventListener('click', function () {// It triggers whenever there is a click on the form
	const buttonId = this.id;// It gets the id from the clicked button
	const roverId = buttonId.split('marsApi')[1];//It parses it, blotting out the marApi from it
	let roverType = document.getElementById("marsApiRoverData");// Getting the hidden input
	roverType.value = roverId;// Assigning the roverId to it
	document.getElementById("frmRoverType").submit();// Submitting the form with the input populated
}));

// let urlParams = new URLSearchParams(window.location.search); It works, he just doesn't understand


//Now it starts the catching of parameter name for showing when a button was clicked and what rover we are seeing

function getUrlParameter(name) {// Method used below
	name = name.replace(/[\[]/, '\\[').replace(/{\]]/, '\\');
	var regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
	var results = regex.exec(location.search);
	return results == null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
}

let marsRoverType = document.getElementById('marsApiRoverData').value;

const highlightButtonByRoverType = (roverType) => {
	
	if(roverType === '') //Default value
		roverType = 'Opportunity';
		
	document.getElementById("marsApi" + roverType).classList.remove('btn-secondary');
	document.getElementById("marsApi" +roverType).classList.add('btn-primary');
}

highlightButtonByRoverType(marsRoverType);

// Now it changes the value of marsDay when clicked and redirected
let marsSol = document.getElementById('marsSol').value;

if(marsSol === null || marsSol === '')//Checking if number is not initatialized
	marsSol = 1;
	
document.getElementById('marsSol').value = marsSol;


