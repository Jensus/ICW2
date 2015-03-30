var projector_names = {
  '1' : 'Dummy Projector_1',
  '2' : 'Dummy Projector_2',
  '3' : 'Dummy Projector_3'
};

var projector_channels = {
  '1' : 'dummy1',
  '2' : 'dummy2',
  '3' : 'dummy3'
};
$(document).ready(function() {
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

function prepareCommand(caller)
{
	var device = '';
	var channel = '';
	var command = '';
	var p1 = '';
	var p2 = '';
	
	var id = $(caller).parent().parent().parent().parent().attr('id');
	device = projector_names[id];
	channel = projector_channels[id];
	
	if($(caller).attr('id') == 'power_on')
	{
		command = 'PowerOn';
	}
	else if($(caller).attr('id') == 'power_off')
	{
		command = 'PowerOff';
	}
	else if($(caller).attr('id') == 'input-brightness')
	{
		command = 'SetBrightness';
		p1 = $(caller).val();
	}
	else if($(caller).attr('id') == 'input-saturation')
	{
		command = 'SetContrast';
		p1 = $(caller).val();
	}
	else if($(caller).attr('id') == 'select-colortemp')
	{
		command = 'SetColorTemperatur';
		p1 = $(caller).val();
	}
	else if($(caller).attr('id') == 'select-source')
	{
		command = 'InputSelect';
		p1 = $(caller).val();
	}
	if (device != '' && channel != '' && command != '')
	{
		sendCommand(device, channel, command, p1, p2);
	}
}

function sendCommand(device, channel, command, p1, p2)
{
	$.get('/sendCommand', {'device': device, 'channel': channel, 'command': command, 'p1': p1, 'p2': p2}, function(data) { alert(data); });
}