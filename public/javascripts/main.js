var projector_names = {
  '1' : 'IN3118HD_1',
  '2' : 'IN3118HD_2',
  '3' : 'IN3118HD_3'
};

var projector_channels = {
  '1' : 'Channel1',
  '2' : 'Channel2',
  '3' : 'Channel3'
};

var input_select = {
	'0' : 'PC',
	'1' : 'DVI-HDMI1',
	'2' : 'HDMI2',
	'3' : 'Video',
	'4' : 'S-Video'
}

var set_color_temp = {
	'0' : 'Warm',
	'1' : 'Normal',
	'2' : 'Cool',
	'3' : 'Cooler',
	'4' : 'High Cool'
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
	
	if($(caller).attr('id') == 'power_on')
	{
		command = 'PowerOn';
		var id = $(caller).parent().parent().parent().parent().attr('id');
	}
	else if($(caller).attr('id') == 'power_off')
	{
		command = 'PowerOff';
		var id = $(caller).parent().parent().parent().parent().attr('id');
	}
	else if($(caller).attr('id') == 'input-brightness')
	{
		command = 'SetBrightness';
		p1 = $(caller).val();
		var id = $(caller).parent().parent().parent().parent().parent().attr('id');
	}
	else if($(caller).attr('id') == 'input-saturation')
	{
		command = 'SetContrast';
		p1 = $(caller).val();
		var id = $(caller).parent().parent().parent().parent().parent().attr('id');
	}
	else if($(caller).attr('id') == 'select-colortemp')
	{
		command = 'SetColorTemperature';
		p1 = set_color_temp[$(caller).val()];
		var id = $(caller).parent().parent().parent().parent().attr('id');
	}
	else if($(caller).attr('id') == 'select-source')
	{
		command = 'InputSelect';
		p1 = input_select[$(caller).val()];
		var id = $(caller).parent().parent().parent().parent().attr('id');
	}
	
	device = projector_names[id];
	channel = projector_channels[id];
	
	if (device != '' && channel != '' && command != '')
	{
		sendCommand(device, channel, command, p1, p2);
	}
}

function sendCommand(device, channel, command, p1, p2)
{
	$.get('/sendCommand', {'device': device, 'channel': channel, 'command': command, 'p1': p1, 'p2': p2}, function(data) { toastr["success"](data); });
}