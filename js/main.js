$(document).ready(function(){

});

function decreaseValue(caller, number)
{
	var input = $(caller).parent().parent().find('input[type="text"]');
	input.val(parseInt(input.val()) - number).trigger('change');
}

function increaseValue(caller, number)
{
	var input = $(caller).parent().parent().find('input[type="text"]');
	input.val(parseInt(input.val()) + number).trigger('change');
}

function correctValue(caller)
{
	var that = $(caller);
	if (parseInt(that.val()) > 100) that.val(100);
	if (parseInt(that.val()) < 0) that.val(0);
}

function sendValue(caller)
{
	console.log('Sende ' + $(caller).prop('name') + ': ' + $(caller).val());
}