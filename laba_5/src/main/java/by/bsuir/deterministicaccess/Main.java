package by.bsuir.deterministicaccess;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends Application {

    private static final int DELAY = 1000;
    private static final int PERIOD = 1000;
    private static final String MARKER = "*";

    private Timer timer;
    private Package aPackage;
    private final byte[] addresses = {1, 10, 100};


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        ArrayList<Station> stations = new ArrayList<>();
        stations.add(new Station(addresses[0], addresses[1], (byte) 1));
        stations.add(new Station(addresses[1], addresses[2], (byte) 0));
        stations.add(new Station(addresses[2], addresses[0], (byte) 0));

        stage = new Stage();
        Stage stage2 = new Stage();
        Stage stage3 = new Stage();

        stage.setX(440);
        stage.setY(320);
        stage2.setX(790);
        stage2.setY(320);
        stage3.setX(1140);
        stage3.setY(320);

        stations.get(0).start(stage);
        stations.get(1).start(stage2);
        stations.get(2).start(stage3);

        timer = new Timer();

        stations.get(0).getStartButton().setOnAction((event) -> {
            aPackage = new Package();
            for (int i = 2; i >= 0; i--) {
                int currentStation = i;

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        System.out.println(currentStation);
                        stationRoutine(stations.get(currentStation), aPackage);
                    }
                }, DELAY, PERIOD);

                stations.get(0).getStartButton().setDisable(true);
            }
        });

        stations.get(0).getTokenText().textProperty().addListener(observable -> sendPackage(stations.get(0)));
        stations.get(1).getTokenText().textProperty().addListener(observable -> sendPackage(stations.get(1)));
        stations.get(2).getTokenText().textProperty().addListener(observable -> sendPackage(stations.get(2)));
    }

    public static void stationRoutine(Station station, Package aPackage) {
        try {
            if (aPackage.getControl() == 0) {
                aPackage.setDestination(station.getDestinationAddress());
            } else {
                runOnUIThread(() -> {
                    if (aPackage.getDestination() == station.getSourceAddress()) {
                        String data = "";
                        data += (char) aPackage.getData();

                        aPackage.setStatus((byte) 1);
                        station.getOutputArea().appendText(data);
                    }
                    if (aPackage.getStatus() == 1 && station.getMonitor() == 1) {
                        aPackage.setControl((byte) 0);
                        aPackage.freeData();
                        aPackage.setStatus((byte) 0);
                    }
                });
            }
            runOnUIThread(() -> station.getTokenText().setText(MARKER));
            Thread.sleep(DELAY);
            runOnUIThread(() -> station.getTokenText().setText(""));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPackage(Station station) {
        if (!station.getInputArea().getText().equals("")
                && !station.getDestination().equals("")
                && aPackage.getControl() == 0
                && aPackage != null) {
            if (station.getTokenText().getText().equals("*")) {
                if (station.getDestination().equals("1")) {
                    station.setDestinationAddress(addresses[0]);
                }
                if (station.getDestination().equals("10")) {
                    station.setDestinationAddress(addresses[1]);
                }
                if (station.getDestination().equals("100")) {
                    station.setDestinationAddress(addresses[2]);
                }

                aPackage.setControl((byte) 1);
                aPackage.setDestination(station.getDestinationAddress());
                String reduced = station.getInputArea().getText().substring(1);
                aPackage.setData(station.getInputArea().getText().getBytes()[0]);
                station.getInputArea().setText(reduced);
            }
        }
    }

    private static void runOnUIThread(Runnable task) {
        if (task == null) throw new NullPointerException("Param task can not be null");

        if (Platform.isFxApplicationThread()) task.run();
        else Platform.runLater(task);
    }
}
