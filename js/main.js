$(document).ready(function(){

});

function decreaseValue(caller, number)
{
	var input = $(caller).parent().parent().find('input[type="text"]');
	input.val(parseInt(input.val()) - 5).trigger('change');
}

function increaseValue(caller, number)
{
	var input = $(caller).parent().parent().find('input[type="text"]');
	input.val(parseInt(input.val()) + 5).trigger('change');
}

function sendValue(caller)
{
	console.log('Sende ' + $(caller).prop('name') + ': ' + $(caller).val());
}