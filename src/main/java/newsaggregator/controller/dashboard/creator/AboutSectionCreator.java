package newsaggregator.controller.dashboard.creator;

import javafx.scene.control.Hyperlink;
import newsaggregator.notification.ErrorNotification;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class AboutSectionCreator implements Creator{
    private Hyperlink aboutDocument;
    private Hyperlink aboutSrc;
    private Hyperlink aboutTutorial;

    public AboutSectionCreator(Hyperlink aboutDocument, Hyperlink aboutSrc, Hyperlink aboutTutorial) {
        this.aboutDocument = aboutDocument;
        this.aboutSrc = aboutSrc;
        this.aboutTutorial = aboutTutorial;
    }

    private void browse (Hyperlink link)  {
        try {
            Desktop.getDesktop().browse(new URI(link.getText()));
        }
        catch (URISyntaxException | IOException e) {
            new ErrorNotification().showMessage("Something went wrong when opening this website");
        }
    }
    @Override
    public void create() {
        aboutDocument.setOnMouseClicked(mouseEvent -> browse(aboutDocument));
        aboutSrc.setOnMouseClicked(mouseEvent -> browse(aboutSrc));
        aboutTutorial.setOnMouseClicked(mouseEvent -> browse(aboutTutorial));
    }
}
