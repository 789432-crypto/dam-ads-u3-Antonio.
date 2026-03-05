package vista.views;

import modelo.*;
import servicio.ClubDeportivo;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class CancelarReservaView extends GridPane {

    public CancelarReservaView(ClubDeportivo club) {

        setPadding(new Insets(12));
        setHgap(8);
        setVgap(8);

        ComboBox<Reserva> combo = new ComboBox<>();
        Button cancelar = new Button("Cancelar reserva");

        // 🔹 Cargar reservas desde BD
        combo.getItems().addAll(club.getReservas());

        // 🔹 Mostrar solo el ID en el ComboBox
        combo.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Reserva item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getIdReserva());
            }
        });

        combo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Reserva item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getIdReserva());
            }
        });

        addRow(0, new Label("Reserva"), combo);
        add(cancelar, 1, 1);

        cancelar.setOnAction(e -> {
            try {

                Reserva seleccionada = combo.getValue();

                if (seleccionada == null) {
                    showError("Debe seleccionar una reserva");
                    return;
                }

                club.cancelarReserva(seleccionada.getIdReserva());

                showInfo("Reserva cancelada correctamente");

                // Refrescar lista
                combo.getItems().clear();
                combo.getItems().addAll(club.getReservas());

            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        a.setHeaderText("Error");
        a.showAndWait();
    }

    private void showInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.showAndWait();
    }
}