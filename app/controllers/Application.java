package controllers;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.Index;
import views.html.WebControl;
import views.formdata.LoginFormData;
import play.mvc.Security;

import utils.MonkeyEncryption;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Implements the controllers for this application.
 */
public class Application extends Controller {

	/**
	 * Provides the Index page.
	 * @return The Index page. 
	 */
	public static Result index() {
		Form<LoginFormData> dummy = new Form(LoginFormData.class).bindFromRequest();
		return ok(Index.render("Home", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx()), dummy));
	}
	
	/**
	 * Processes a login form submission from an unauthenticated user. 
	 * First we bind the HTTP POST data to an instance of LoginFormData.
	 * The binding process will invoke the LoginFormData.validate() method.
	 * If errors are found, re-render the page, displaying the error data. 
	 * If errors not found, render the page with the good data. 
	 * @return The index page with the results of validation. 
	 */
	public static Result postLogin() {

		// Get the submitted form data from the request object, and run validation.
		Form<LoginFormData> dummy = new Form(LoginFormData.class).bindFromRequest();

		LoginFormData formData = new LoginFormData();
		formData.email = Form.form().bindFromRequest().get("email");
		formData.password = MonkeyEncryption.toSHA256(Form.form().bindFromRequest().get("password"));

		if (formData.validate().size() > 0) {
			flash("error", "Login credentials not valid.");
			return badRequest(Index.render("Login", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx()), dummy));
		}
		else {
			// email/password OK, so now we set the session variable and only go to authenticated pages.
			session().clear();
			session("email", formData.email);
			return redirect(routes.Application.webControl());
		}
	}
	
	/**
	 * Logs out (only for authenticated users) and returns them to the Index page. 
	 * @return A redirect to the Index page. 
	 */
	@Security.Authenticated(Secured.class)
	public static Result logout() {
		session().clear();
		//return redirect(routes.Application.index());
		return redirect(controllers.routes.Application.index());
	}
	
	public static Map splitVControlResponse(String response) {
		Map responseMap = new HashMap();

		String[] splittedResponse = response.split(""+(char)5, -1);
		String responseType = splittedResponse[0];
		String eventModule = splittedResponse[1];
		String eventParams = splittedResponse[2];
		splittedResponse = eventParams.split(""+(char)2, -1);
		String eventDevice = splittedResponse[0];
		String eventChannel = splittedResponse[1];
		String eventCmd = splittedResponse[2];
		String eventData = splittedResponse[9];

		responseMap.put("MsgType", responseType);
		responseMap.put("EventModule", eventModule);
		responseMap.put("EventDevice", eventDevice);
		responseMap.put("EventChannel", eventChannel);
		responseMap.put("EventCmd", eventCmd);
		responseMap.put("EventData", eventData);
		System.out.println(responseMap);
		return responseMap;
	}

	/**
	 * Provides the webControl page (only to authenticated users).
	 * @return The webControl page. 
	 */
	@Security.Authenticated(Secured.class)
	public static Result webControl() {
		/*String sentence;
		byte[] response = new byte[256];
		try
		{
			BufferedReader inFromUser = new BufferedReader( new InputStreamReader(System.in));
			Socket vcontrol = new Socket("127.0.0.1", 10101);
			DataOutputStream outToServer = new DataOutputStream(vcontrol.getOutputStream());
			DataInputStream inFromServer = new DataInputStream(vcontrol.getInputStream());
			vcontrol.setSoTimeout(5000);

			String deviceName = "Dummy Projector_2";
			String channelList = "dummy2";
			String command = "GetVolume";
			String dp1 = "";
			String dp2 = "";
			String p1 = "";
			String p2 = "";
			String p3 = "";
			String p4 = ""; //Dummy Projector_2', 'dummy2', 'GetPower'

			sentence = (char)4 + "0" + (char)5 + "devRunCommand" + (char)5 + deviceName + (char)2 + channelList + (char)2 + command + (char)2 + dp1 + (char)2 + dp2 + (char)2 + p1 + (char)2 + p2 + (char)2 + p3 + (char)2 + p4 + (char)6;
			if (vcontrol.isConnected())
			{
				outToServer.writeBytes(sentence);
				System.out.println("Wrote Command to Server.");
				System.out.println("Command: " + sentence);
			}
			else
				System.out.println("Not connected.");
			inFromServer.read(response);
			String str = new String(response, "UTF-8");
			System.out.println("FROM SERVER: " + str);
			Map foo = splitVControlResponse(str);
			vcontrol.close();
		}
		catch (Exception ex){ System.out.println("Socket Exception: " + ex); }*/
		return ok(WebControl.render("webControl", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx())));
	}

	@Security.Authenticated(Secured.class)
	public static Result sendCommand(String device, String channel, String command, String p1, String p2)
	{
		System.out.println("Device:" + device + "; Channel:" + channel + "; Command:" + command + "; p1: " + p1 + "; p2: " + p2);
		return ok("Hallo " + p1 + " " + p2 + "!");
	}
}
