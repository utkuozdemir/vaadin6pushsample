package org.utkuozdemir.vaadin6pushsample;

import com.vaadin.Application;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.addons.serverpush.ServerPush;

/**
 * Created on 30.10.2014.
 *
 * @author Utku
 */
@SuppressWarnings("serial")
public class SampleApplication extends Application {
    private static final Logger logger = LoggerFactory.getLogger(SampleApplication.class);

    private VerticalLayout labelLayout;

    private ServerPush pusher;

    private int counter = 1;

    @Override
    public void init() {
        pusher = new ServerPush();

        Window window = new Window("Sample Push Application");
        setMainWindow(window);

        Label label = new Label("Welcome to Vaadin 6 ServerPush Test Application!");
        Button startButton = new Button("Push Test");
        startButton.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                labelLayout.addComponent(new Label("-------------------------"));
                labelLayout.addComponent(new Label("Wait 5 seconds..."));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        doWork();
                    }
                }).start();
            }
        });

        Button clearButton = new Button("Clear");
        clearButton.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                labelLayout.removeAllComponents();
            }
        });

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setMargin(true);
        mainLayout.setSpacing(true);
        mainLayout.setImmediate(true);

        mainLayout.addComponent(label);
        mainLayout.addComponent(pusher);

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.addComponent(startButton);
        buttonsLayout.addComponent(clearButton);

        mainLayout.addComponent(buttonsLayout);

        labelLayout = new VerticalLayout();
        mainLayout.addComponent(labelLayout);

        window.setContent(mainLayout);
    }

    private void doWork() {
        try {
            Thread.sleep(5000l);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
        synchronized (this) {
            Label l = new Label("This label is pushed to this screen " + "(" + counter++ + ")");
            labelLayout.addComponent(l);
        }
        this.pusher.push();
    }


}
