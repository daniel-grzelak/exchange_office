package com.daniel.exchangeoffice.ui;


import com.daniel.exchangeoffice.DAO.UserDAO;
import com.daniel.exchangeoffice.classes.DataNBP;
import com.daniel.exchangeoffice.classes.GridCurrencyModel;
import com.daniel.exchangeoffice.classes.User;
import com.vaadin.data.HasValue;
import com.vaadin.navigator.View;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class MyUI extends VerticalLayout implements View {

    @Autowired
    private UserDAO userDAO;
    private static Grid<GridCurrencyModel> grid = new Grid<>();
    private static HorizontalLayout buttonsRow = new HorizontalLayout();


    public MyUI() {
        setSizeFull();


        DataNBP.checkCurrency();
        gridMaker();
        createButtonsRow();
        addComponents(grid, buttonsRow);
        setComponentAlignment(grid, Alignment.MIDDLE_CENTER);
        setComponentAlignment(buttonsRow, Alignment.MIDDLE_CENTER);


    }

    private void gridMaker() {
        List<GridCurrencyModel> list = Arrays.asList(
                new GridCurrencyModel("GBP:", DataNBP.getRateGBP().get(DataNBP.getRateGBP().size() - 1)),
                new GridCurrencyModel("USD:", DataNBP.getRateUSD().get(DataNBP.getRateUSD().size() - 1)),
                new GridCurrencyModel("EUR:", DataNBP.getRateUSD().get(DataNBP.getRateEUR().size() - 1)));
        grid.setItems(list);
        grid.addColumn(GridCurrencyModel::getName).setCaption("Name: ").setResizable(false);
        grid.addColumn(GridCurrencyModel::getExchangeRate).setCaption("Exchange rate: ").setResizable(false);
        grid.setHeightByRows(list.size());


    }

    private void createButtonsRow() {
        List<String> options = new ArrayList<>(Arrays.asList("Buy", "Sell", "Exchange"));
        RadioButtonGroup radioButtonGroup = new RadioButtonGroup("Choose an option", options);
        radioButtonGroup.setSelectedItem(options.get(0));
        List<String> currencies = DataNBP.currencyNames();
        NativeSelect nativeSelect = new NativeSelect("Choose currency:", currencies);
        nativeSelect.setEmptySelectionAllowed(false);
        nativeSelect.setSelectedItem(0);
        TextField textField = new TextField();
        Label label = new Label("to");
        NativeSelect nativeSelect1 = new NativeSelect("Choose currency:", currencies);
        nativeSelect1.setEmptySelectionAllowed(false);
        nativeSelect1.setValue("USD");
        TextField textField1 = new TextField();
        textField1.setEnabled(false);
        Button doIt = new Button("Do it!");

        nativeSelect.addValueChangeListener((HasValue.ValueChangeListener) valueChangeEvent -> nativeSelect1.setItems(currencies.stream().filter(e -> !e.equals(valueChangeEvent.getValue())).collect(Collectors.toList())));
        nativeSelect1.addValueChangeListener((HasValue.ValueChangeListener) valueChangeEvent -> nativeSelect.setItems(currencies.stream().filter(e -> !e.equals(valueChangeEvent.getValue())).collect(Collectors.toList())));


        doIt.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                User user = (User) getUI().getSession().getAttribute("User");
                if (radioButtonGroup.getSelectedItem().get().equals("Buy")) {

                    if (user.getPln() >= Integer.parseInt(textField.getValue())) {

                        if (nativeSelect.getSelectedItem().get().equals(0)) {
                            System.out.println(Long.parseLong(textField.getValue()) * Long.parseLong(DataNBP.getRateGBP().get(DataNBP.getRateGBP().size() - 1)));
                        } else if (nativeSelect.getSelectedItem().equals("USD")) {

                        } else {

                        }

                    }
                } else if (radioButtonGroup.getSelectedItem().get().equals("Sell")) {

                } else {

                }

            }
        });

        buttonsRow.addComponents(radioButtonGroup, nativeSelect, textField, label, nativeSelect1, textField1, doIt);


    }
}
