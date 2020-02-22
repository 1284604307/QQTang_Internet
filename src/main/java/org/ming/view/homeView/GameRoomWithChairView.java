package org.ming.view.homeView;

import javafx.scene.layout.AnchorPane;
import org.ming.connect.model.User;
import org.ming.controller.ConnectController;

import java.util.ArrayList;

public class GameRoomWithChairView extends AnchorPane {

    private SeatView[] seatViews ;
    public GameRoomWithChairView() {
        super();

        this.setPrefWidth(520);
        this.setPrefWidth(520);
        this.setStyle("-fx-background-color: #8f248455;");
        this.setLayoutX(0);
        this.setLayoutY(20);
        seatViews = new SeatView[]{
            new SeatView(0,0),
            new SeatView(0,1),
            new SeatView(0,2),
            new SeatView(0,3),
            new SeatView(1,0),
            new SeatView(1,1),
            new SeatView(1,2),
            new SeatView(1,3),
        };
        for (SeatView view : seatViews) {
            this.getChildren().add(view);
        }

    }

    public void update(ArrayList<User> users){
        for (SeatView seatView : seatViews) {
            seatView.setUser(null);
        }
        for (User user : users) {
            addUser(user);
        }

    }

    public void addUser(User user){
        for (SeatView seatView : seatViews) {
            if (seatView.empty()) {
                seatView.setUser(user);
                break;
            }
        }
    }

    public SeatView[] getSeatViews() {
        return seatViews;
    }

    public void setSeatViews(SeatView[] seatViews) {
        this.seatViews = seatViews;
    }

}
