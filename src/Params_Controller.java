import DialogBoxes.ErrorBox_Controller;
import DialogBoxes.SuccessBox_Controller;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Params_Controller {

    @FXML private ImageView productKeyExpander;
    @FXML private BorderPane productKeyExtensionBPane;

    @FXML private ImageView accountExpander;
    @FXML private BorderPane accountExtensionBPane;

    @FXML private ImageView appearanceExpander;
    @FXML private BorderPane appearanceExtensionBPane;

    @FXML private Label surnameLabel;
    @FXML private Label firstnameLabel;
    @FXML private Label mobileLabel;
    @FXML private Label emailLabel;
    @FXML private Label deleteAccountLabel;

    @FXML private Button sendCodeButton;
    @FXML private Button saveEmailButton;
    @FXML private Button savePwdButton;
    @FXML private Button deleteAccountButton;

    @FXML private TextField newEmailTField;
    @FXML private PasswordField codePField;
    @FXML private PasswordField ancientPwdPField;
    @FXML private PasswordField newPwdPField;
    @FXML private PasswordField deleteAccountPField;

    private ArrayList<ImageView> expanders = new ArrayList<>();
    private ArrayList<BorderPane> extensions = new ArrayList<>();
    private ArrayList<Boolean> expandersState = new ArrayList<>(); // false -> not expanded| true -> expanded


    // non FXML attributes

    private String genratedCode;
    private String providedEmail;

    @FXML public void initialize()
    {

        syncUserInfos();

        expanders.add(productKeyExpander);
        extensions.add(productKeyExtensionBPane);

        expanders.add(accountExpander);
        extensions.add(accountExtensionBPane);

        expanders.add(appearanceExpander);
        extensions.add(appearanceExtensionBPane);

        expandersState.add(false);
        expandersState.add(false);
        expandersState.add(false);

        for(ImageView img : expanders)
        {
            img.setOnMouseClicked(mouseEvent -> {
                int expanderID = expanders.indexOf(img);
                boolean isExpanded = getState(img);
                String newImgPath = Settings.projectPath + "img\\icons\\" + (isExpanded?"expand.png":"unexpand.png");
                Image image = null;
                try {
                    image = new Image(new FileInputStream(newImgPath));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                img.setImage(image);
                BorderPane ext = getExtension(img);
                Design.setVisible(ext, !isExpanded);
                expandersState.set(expanderID, !isExpanded); // toggle state
            });
        }

        sendCodeButton.setOnMouseClicked(mouseEvent -> {
            if(Settings.ACTIVE_EMAIL_CONFIRM)
            {
                String email = newEmailTField.getText();
                if(email.equals(LoggedIn_Controller.getUser().getEmail()))
                    ErrorBox_Controller.showErrorBox(Settings.appStage, "Erreur", "L'email que vous avez fourni est le meme que l'ancien. Veuillez choisir un autre!");
                else
                {
                    Boolean isValid = true;
                    if(isValid)
                    {
                        providedEmail = email;
                        this.genratedCode = EmailConfirm_Controller.generate6Digits();
                        MailSender ms = new MailSender(Settings.CodeWinEmail, Settings.CodeWinEmailPwd);
                        boolean sentState = ms.sendConfirmationMail(email, genratedCode);
                        if(!sentState)
                            ErrorBox_Controller.showErrorBox(Settings.appStage, "Erreur", "Une erreur est survenue lors de l'envoi du code de confirmation");
                    }
                    else
                        ErrorBox_Controller.showErrorBox(Settings.appStage, "Email invalide!", "Veuillez verifiez l'addresse email entree");
                }

            }
        });

        saveEmailButton.setOnMouseClicked(mouseEvent -> {
            if(providedEmail == null || genratedCode == null)
            {

            }
            else
            {
                String enteredCode = codePField.getText();

                if(enteredCode.equals(genratedCode))
                {
                    User u = LoggedIn_Controller.getUser();
                    boolean success = true;
                    if(Settings.ACTIVE_DB_MODE)
                        success = User.updateEmail(u, providedEmail);
                    if(success)
                    {
                        SuccessBox_Controller.showSuccessBox(Settings.appStage, "Succes", "Votre email a ete mise a jour avec succes");
                        LoggedIn_Controller.getUser().setEmail(providedEmail);
                        emailLabel.setText(providedEmail); // empty
                    }
                    else
                        ErrorBox_Controller.showErrorBox(Settings.appStage, "Erreur", "Une erreur est survenue lors de la mise a jour de votre email");
                }
                else
                    ErrorBox_Controller.showErrorBox(Settings.appStage, "Erreur", "Code invalide");

                // empty
                newEmailTField.setText("");
                codePField.setText("");
            }

            providedEmail = null;
            genratedCode = null;
        });

        savePwdButton.setOnMouseClicked(mouseEvent -> {
            String ancientProvided = ancientPwdPField.getText();
            String newPwd = newPwdPField.getText();
            User u = LoggedIn_Controller.getUser();

            if(newPwd.equals(u.getPassword()))
                ErrorBox_Controller.showErrorBox(Settings.appStage, "Erreur", "Le mot de passe fourni est le meme que l'ancien. Veuillez choisir un autre!");
            else if(ancientProvided.equals(u.getPassword()))
            {
                boolean state = User.updatePassword(u, newPwd);
                if(state)
                    SuccessBox_Controller.showSuccessBox(Settings.appStage, "Success", "Votre mot de passe a ete mis a jour avec succees!");
                else
                    ErrorBox_Controller.showErrorBox(Settings.appStage, "Erreur", "Une erreur est survenue lors de la mise a jour de votre mot de passe. Veuillez Reessayer!");
            }
            else
                Checker.showPwdError();

            ancientPwdPField.setText("");
            newPwdPField.setText("");
        });

        deleteAccountButton.setOnMouseClicked(mouseEvent -> {
            User u = LoggedIn_Controller.getUser();
            String providedPwd = deleteAccountPField.getText();
            if(providedPwd.equals(u.getPassword()))
            {
                boolean state = User.deleteUser(u);
                if(state)
                {
                    SceneLoader.loadAuthenticator();
                }
                else
                    ErrorBox_Controller.showErrorBox(Settings.appStage, "Erreur", "Une erreur est survenue lors de la suppression de votre compte. Veuillez Reessayer!");

            }
            else
                Checker.showPwdError();
        });

        Design.setVisible(productKeyExtensionBPane, false);
        Design.setVisible(accountExtensionBPane, false);
        Design.setVisible(appearanceExtensionBPane, false);
    }

    private boolean getState(ImageView img)
    {
        int i = expanders.indexOf(img);
        return expandersState.get(i);
    }

    private BorderPane getExtension(ImageView img)
    {
        int i = expanders.indexOf(img);
        return extensions.get(i);
    }

    private void syncUserInfos()
    {
        User u = LoggedIn_Controller.getUser();

        firstnameLabel.setText(u.getFirstname());
        surnameLabel.setText(u.getLastname());
        mobileLabel.setText(u.getMobile());
        emailLabel.setText(u.getEmail());
    }
}
