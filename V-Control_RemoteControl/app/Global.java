import models.UserInfoDB;
import play.Application;
import play.GlobalSettings;

/**
 * Provide initialization code for the digits application.
 * @author Philip Johnson
 */
public class Global extends GlobalSettings {

  /**
   * Initialize the system with some sample contacts.
   * @param app The application.
   */
  public void onStart(Application app) {
    UserInfoDB.addUserInfo("John Smith", "smith@example.com", "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8");
    UserInfoDB.addUserInfo("Arsch Wasser", "a@b.de", "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8");
  }
}
