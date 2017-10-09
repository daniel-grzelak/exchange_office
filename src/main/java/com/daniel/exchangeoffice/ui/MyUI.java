package com.daniel.exchangeoffice.ui;


import com.daniel.exchangeoffice.DAO.UserDAO;
import com.daniel.exchangeoffice.classes.DataNBP;
import com.daniel.exchangeoffice.classes.GridCurrencyModel;
import com.daniel.exchangeoffice.classes.User;
import com.vaadin.data.HasValue;
import com.vaadin.navigator.View;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.math.BigDecimal;
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
                new GridCurrencyModel("GBP:", DataNBP.getRateGBP().get(DataNBP.getRateGBP().size() - 1).toString()),
                new GridCurrencyModel("USD:", DataNBP.getRateUSD().get(DataNBP.getRateUSD().size() - 1).toString()),
                new GridCurrencyModel("EUR:", DataNBP.getRateUSD().get(DataNBP.getRateEUR().size() - 1).toString()));
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
        textField.setValueChangeMode(ValueChangeMode.EAGER); //czemu tutaj tylko w GBP jest dynamika

        textField.addValueChangeListener((HasValue.ValueChangeListener<String>) valueChangeEvent -> {
            if (nativeSelect.getSelectedItem().get().equals(0)) {
                textField1.setValue(new BigDecimal(textField.getValue()).multiply(DataNBP.getRateGBP().get(DataNBP.getRateGBP().size() - 1)).toString());
            } else if (nativeSelect.getSelectedItem().get().equals(1)) {
                textField1.setValue(new BigDecimal(textField.getValue()).multiply(DataNBP.getRateEUR().get(DataNBP.getRateEUR().size() - 1)).toString());

            } else if (nativeSelect.getSelectedItem().get().equals(2)) {
                textField1.setValue(new BigDecimal(textField.getValue()).multiply(DataNBP.getRateUSD().get(DataNBP.getRateUSD().size() - 1)).toString());

            }
        });

        Button doIt = new Button("Do it!");

        nativeSelect.addValueChangeListener((HasValue.ValueChangeListener) valueChangeEvent -> nativeSelect1.setItems(currencies.stream().filter(e -> !e.equals(valueChangeEvent.getValue())).collect(Collectors.toList())));
        nativeSelect1.addValueChangeListener((HasValue.ValueChangeListener) valueChangeEvent -> nativeSelect.setItems(currencies.stream().filter(e -> !e.equals(valueChangeEvent.getValue())).collect(Collectors.toList())));


        doIt.addClickListener(new Button.ClickListener() {
            @Override
            @Transactional
            public void buttonClick(Button.ClickEvent clickEvent) {
                User user = (User) getUI().getSession().getAttribute("User");
                if (radioButtonGroup.getSelectedItem().get().equals("Buy")) {

                    if (user.getPln().compareTo(new BigDecimal(textField.getValue()).multiply(DataNBP.getRateGBP().get(DataNBP.getRateGBP().size() - 1))) == 1 || user.getPln().compareTo(new BigDecimal(textField.getValue()).multiply(DataNBP.getRateGBP().get(DataNBP.getRateGBP().size() - 1))) == 0) {

                        if (nativeSelect.getSelectedItem().get().equals(0)) {
                            user.setPln(user.getPln().subtract(new BigDecimal(textField.getValue()).multiply(DataNBP.getRateGBP().get(DataNBP.getRateGBP().size() - 1))));
                            user.setGbp(user.getGbp().subtract(new BigDecimal(textField.getValue())));
                            System.out.println(user);
                            userDAO.save(user);
                        } else if (nativeSelect.getSelectedItem().get().equals(1)) {

                        } else if (nativeSelect.getSelectedItem().get().equals(2)) {

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
