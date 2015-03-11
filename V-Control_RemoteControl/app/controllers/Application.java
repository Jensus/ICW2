package controllers;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.Index;
import views.html.Profile;
import views.html.Login;
import views.formdata.LoginFormData;
import play.mvc.Security;

import utils.MonkeyEncryption;

import java.io.*;
import java.net.*;

/**
 * Implements the controllers for this application.
 */
public class Application extends Controller {

	/**
	 * Provides the Index page.
	 * @return The Index page. 
	 */
	public static Result index() {
		return ok(Index.render("Home", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx())));
	}
	
	/**
	 * Provides the Login page (only to unauthenticated users). 
	 * @return The Login page. 
	 */
	public static Result login() {
		Form<LoginFormData> formData = Form.form(LoginFormData.class);
		return ok(Login.render("Login", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx()), formData));
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
			return badRequest(Login.render("Login", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx()), dummy));
		}
		else {
			// email/password OK, so now we set the session variable and only go to authenticated pages.
			session().clear();
			session("email", formData.email);
			return redirect(routes.Application.profile());
		}
	}
	
	/**
	 * Logs out (only for authenticated users) and returns them to the Index page. 
	 * @return A redirect to the Index page. 
	 */
	@Security.Authenticated(Secured.class)
	public static Result logout() {
		session().clear();
		return redirect(routes.Application.index());
	}
	
	/**
	 * Provides the Profile page (only to authenticated users).
	 * @return The Profile page. 
	 */
	@Security.Authenticated(Secured.class)
	public static Result profile() {
		String sentence;
		byte[] response = new byte[256];
		try
		{
			BufferedReader inFromUser = new BufferedReader( new InputStreamReader(System.in));
			Socket vcontrol = new Socket("141.45.207.253", 10101);
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
			vcontrol.close();
		}
		catch (Exception ex){ System.out.println("Socket Exception: " + ex); }
		return ok(Profile.render("Profile", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx())));
	}
}
