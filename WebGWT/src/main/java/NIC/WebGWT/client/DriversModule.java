package NIC.WebGWT.client;

import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import NIC.WebGWT.shared.Driver;

/**
 * Точка входа клиентской части: выбор команды и вывод списка пилотов.
 */
public class DriversModule implements EntryPoint {
    private static final String SRV_ERR = "Ошибка сервера! ";
    private static final String SRV_ERR_GET_TEAM_LIST = "Невозможно получить список команд.";
    private static final String SRV_ERR_GET_DRIVER_LIST = "Невозможно получить список пилотов.";
    private static final String SHOW_BTN = "Показать пилотов";
    private static final String CLOSE_BTN = "Закрыть";
    private static final String ALL_TEAMS = "Все команды";
    private static final String DRIVERS_WND_TITLE = "Пилоты Формулы-1: ";

    /** Прокси для вызова серверного сервиса. */
    private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

    /** Метод вызывается GWT при загрузке HTML-страницы. */
    public void onModuleLoad() {
        final ListBox teamListBox = new ListBox(false);
        final Label errorLabel = new Label();
        final Button sendButton = new Button(SHOW_BTN);
        sendButton.addStyleName("sendButton");

        RootPanel.get("teamListBoxContainer").add(teamListBox);
        RootPanel.get("errorLabelContainer").add(errorLabel);
        RootPanel.get("sendButtonContainer").add(sendButton);

        // Первый пункт списка: все команды (пустое значение)
        teamListBox.addItem(ALL_TEAMS, "");
        teamListBox.setFocus(true);

        // Запрос списка команд у сервера
        greetingService.getTeamList(new AsyncCallback<List<String>>() {
            public void onFailure(Throwable caught) {
                errorLabel.setText(SRV_ERR + SRV_ERR_GET_TEAM_LIST);
            }

            public void onSuccess(List<String> result) {
                for (String team : result) {
                    teamListBox.addItem(team, team);
                }
            }
        });

        // Диалоговое окно с таблицей пилотов
        final DialogBox dialogBox = new DialogBox();
        dialogBox.setText(DRIVERS_WND_TITLE);
        dialogBox.setAnimationEnabled(true);
        final Button closeButton = new Button(CLOSE_BTN);
        closeButton.getElement().setId("closeButton");

        VerticalPanel dialogVPanel = new VerticalPanel();
        dialogVPanel.addStyleName("dialogVPanel");
        final CellTable<Driver> table = createCellTable();
        dialogVPanel.add(table);
        dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
        dialogVPanel.add(closeButton);
        dialogBox.setWidget(dialogVPanel);

        closeButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                dialogBox.hide();
                sendButton.setEnabled(true);
                sendButton.setFocus(true);
            }
        });

        // Обработчик кнопки: запрос пилотов выбранной команды
        sendButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                errorLabel.setText("");
                int index = teamListBox.getSelectedIndex();
                final String teamValue = teamListBox.getValue(index);
                final String teamTitle = teamListBox.getItemText(index);
                sendButton.setEnabled(false);

                greetingService.getDriverList(teamValue, new AsyncCallback<List<Driver>>() {
                    public void onFailure(Throwable caught) {
                        errorLabel.setText(SRV_ERR + SRV_ERR_GET_DRIVER_LIST);
                        sendButton.setEnabled(true);
                    }

                    public void onSuccess(List<Driver> result) {
                        dialogBox.setText(DRIVERS_WND_TITLE + teamTitle);
                        table.setRowCount(result.size(), true);
                        table.setRowData(0, result);
                        dialogBox.center();
                        closeButton.setFocus(true);
                    }
                });
            }
        });
    }

    /** Создаёт таблицу с колонками: пилот, команда, номер, титулы. */
    private CellTable<Driver> createCellTable() {
        final CellTable<Driver> table = new CellTable<Driver>();
        table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);

        TextColumn<Driver> nameColumn = new TextColumn<Driver>() {
            public String getValue(Driver object) {
                return object.getName();
            }
        };
        table.addColumn(nameColumn, "Пилот");

        TextColumn<Driver> teamColumn = new TextColumn<Driver>() {
            public String getValue(Driver object) {
                return object.getTeam();
            }
        };
        table.addColumn(teamColumn, "Команда");

        TextColumn<Driver> numberColumn = new TextColumn<Driver>() {
            public String getValue(Driver object) {
                return String.valueOf(object.getNumber());
            }
        };
        table.addColumn(numberColumn, "Номер");

        TextColumn<Driver> titlesColumn = new TextColumn<Driver>() {
            public String getValue(Driver object) {
                return String.valueOf(object.getTitles());
            }
        };
        table.addColumn(titlesColumn, "Титулы");

        return table;
    }
}