package com.daniel.exchangeoffice.ui;


import com.daniel.exchangeoffice.DAO.UserDAO;
import com.daniel.exchangeoffice.classes.DataNBP;
import com.daniel.exchangeoffice.classes.GridCurrencyModel;
import com.daniel.exchangeoffice.classes.User;
import com.vaadin.data.HasValue;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.*;


import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class MyUI extends VerticalLayout implements View {

    private UserDAO userDAO;
    private static Grid<GridCurrencyModel> grid = new Grid<>();
    private static HorizontalLayout buttonsRow = new HorizontalLayout();

    public MyUI(UserDAO userDAO) {
        this.userDAO = userDAO;

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
                new GridCurrencyModel("EUR:", DataNBP.getRateEUR().get(DataNBP.getRateEUR().size() - 1).toString()));
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
        NativeSelect<String> nativeSelect = new NativeSelect("Choose currency:", currencies);
        nativeSelect.setEmptySelectionAllowed(false);
        nativeSelect.setSelectedItem("GBP");
        TextField textField = new TextField();
        Label label = new Label("to");
        NativeSelect nativeSelect1 = new NativeSelect("Choose currency:", currencies);
        nativeSelect1.setEmptySelectionAllowed(false);
        nativeSelect1.setValue("USD");
        TextField textField1 = new TextField();
        textField1.setEnabled(false);
        textField.setValueChangeMode(ValueChangeMode.EAGER);


        textField.addValueChangeListener((HasValue.ValueChangeListener<String>) valueChangeEvent -> {
            try {
                if (radioButtonGroup.getSelectedItem().get().equals("Buy") || radioButtonGroup.getSelectedItem().get().equals("Sell")) {
                    if (nativeSelect.getSelectedItem().get().equals("GBP")) {
                        textField1.setValue(new BigDecimal(textField.getValue()).multiply(DataNBP.getRateGBP().get(DataNBP.getRateGBP().size() - 1)).toString());
                    } else if (nativeSelect.getSelectedItem().get().equals("EUR")) {
                        textField1.setValue(new BigDecimal(textField.getValue()).multiply(DataNBP.getRateEUR().get(DataNBP.getRateEUR().size() - 1)).toString());
                    } else if (nativeSelect.getSelectedItem().get().equals("USD")) {
                        textField1.setValue(new BigDecimal(textField.getValue()).multiply(DataNBP.getRateUSD().get(DataNBP.getRateUSD().size() - 1)).toString());
                    }
                } else if (radioButtonGroup.getSelectedItem().get().equals("Exchange")) {
                    if (nativeSelect.getSelectedItem().get().equals("GBP")) {
                        if (nativeSelect1.getSelectedItem().get().equals("EUR")) {
                            textField1.setValue(new BigDecimal(textField.getValue()).multiply(DataNBP.getRateGBP().get(DataNBP.getRateGBP().size() - 1)).divide(DataNBP.getRateEUR().get(DataNBP.getRateEUR().size() - 1), 8, RoundingMode.HALF_EVEN).toString());
                        } else if (nativeSelect1.getSelectedItem().get().equals("USD")) {
                            textField1.setValue(new BigDecimal(textField.getValue()).multiply(DataNBP.getRateGBP().get(DataNBP.getRateGBP().size() - 1)).divide(DataNBP.getRateUSD().get(DataNBP.getRateUSD().size() - 1), 8, RoundingMode.HALF_EVEN).toString());
                        }
                    } else if (nativeSelect.getSelectedItem().get().equals("EUR")) {
                        if (nativeSelect1.getSelectedItem().get().equals("GBP")) {
                            textField1.setValue(new BigDecimal(textField.getValue()).multiply(DataNBP.getRateEUR().get(DataNBP.getRateEUR().size() - 1)).divide(DataNBP.getRateGBP().get(DataNBP.getRateGBP().size() - 1), 8, RoundingMode.HALF_EVEN).toString());
                        } else if (nativeSelect1.getSelectedItem().get().equals("USD")) {
                            textField1.setValue(new BigDecimal(textField.getValue()).multiply(DataNBP.getRateEUR().get(DataNBP.getRateEUR().size() - 1)).divide(DataNBP.getRateUSD().get(DataNBP.getRateUSD().size() - 1), 8, RoundingMode.HALF_EVEN).toString());
                        }
                    } else if (nativeSelect.getSelectedItem().get().equals("USD")) {
                        if (nativeSelect1.getSelectedItem().get().equals("GBP")) {
                            textField1.setValue(new BigDecimal(textField.getValue()).multiply(DataNBP.getRateUSD().get(DataNBP.getRateUSD().size() - 1)).divide(DataNBP.getRateGBP().get(DataNBP.getRateGBP().size() - 1), 8, RoundingMode.HALF_EVEN).toString());
                        } else if (nativeSelect1.getSelectedItem().get().equals("EUR")) {
                            textField1.setValue(new BigDecimal(textField.getValue()).multiply(DataNBP.getRateUSD().get(DataNBP.getRateUSD().size() - 1)).divide(DataNBP.getRateEUR().get(DataNBP.getRateEUR().size() - 1), 8, RoundingMode.HALF_EVEN).toString());
                        }
                    }
                }
            } catch (NumberFormatException n) {
                textField.clear();
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
                    System.out.println("x");
                    if (nativeSelect.getSelectedItem().get().equals("GBP")) {
                            user.setPln(user.getPln().subtract(new BigDecimal(textField.getValue()).multiply(DataNBP.getRateGBP().get(DataNBP.getRateGBP().size() - 1))));
                        user.setGbp(user.getGbp().add(new BigDecimal(textField.getValue())));
                            userDAO.save(user);
                    } else if (nativeSelect.getSelectedItem().get().equals("EUR")) {
                        user.setPln(user.getPln().subtract(new BigDecimal(textField.getValue()).multiply(DataNBP.getRateEUR().get(DataNBP.getRateEUR().size() - 1))));
                        user.setEur(user.getEur().add(new BigDecimal(textField.getValue())));
                        userDAO.save(user);
                    } else if (nativeSelect.getSelectedItem().get().equals("USD")) {
                        user.setPln(user.getPln().subtract(new BigDecimal(textField.getValue()).multiply(DataNBP.getRateUSD().get(DataNBP.getRateUSD().size() - 1))));
                        user.setUsd(user.getUsd().add(new BigDecimal(textField.getValue())));
                        userDAO.save(user);
                    }
                } else if (radioButtonGroup.getSelectedItem().get().equals("Sell")) {
                    if (nativeSelect.getSelectedItem().get().equals("GBP")) {
                        user.setPln(user.getPln().add(new BigDecimal(textField.getValue()).multiply(DataNBP.getRateGBP().get(DataNBP.getRateGBP().size() - 1))));
                        user.setGbp(user.getGbp().subtract(new BigDecimal(textField.getValue())));
                        userDAO.save(user);
                    } else if (nativeSelect.getSelectedItem().get().equals("EUR")) {
                        user.setPln(user.getPln().add(new BigDecimal(textField.getValue()).multiply(DataNBP.getRateEUR().get(DataNBP.getRateEUR().size() - 1))));
                        user.setEur(user.getEur().subtract(new BigDecimal(textField.getValue())));
                        userDAO.save(user);
                    } else if (nativeSelect.getSelectedItem().get().equals("USD")) {
                        user.setPln(user.getPln().add(new BigDecimal(textField.getValue()).multiply(DataNBP.getRateUSD().get(DataNBP.getRateUSD().size() - 1))));
                        user.setUsd(user.getUsd().subtract(new BigDecimal(textField.getValue())));
                        userDAO.save(user);
                    }
                } else {
                    if (nativeSelect.getSelectedItem().get().equals("GBP")) {
                        user.setGbp(user.getGbp().subtract(new BigDecimal(textField.getValue())));
                        if (nativeSelect1.getSelectedItem().get().equals("EUR")) {
                            user.setEur(new BigDecimal(textField.getValue()).multiply(DataNBP.getRateGBP().get(DataNBP.getRateGBP().size() - 1)).divide(DataNBP.getRateEUR().get(DataNBP.getRateEUR().size() - 1), 8, RoundingMode.HALF_EVEN));
                            userDAO.save(user);
                        } else if (nativeSelect1.getSelectedItem().get().equals("USD")) {
                            user.setUsd(new BigDecimal(textField.getValue()).multiply(DataNBP.getRateGBP().get(DataNBP.getRateGBP().size() - 1)).divide(DataNBP.getRateUSD().get(DataNBP.getRateUSD().size() - 1), 8, RoundingMode.HALF_EVEN));
                            userDAO.save(user);
                        }
                    } else if (nativeSelect.getSelectedItem().get().equals("EUR")) {
                        user.setEur(user.getEur().subtract(new BigDecimal(textField.getValue())));
                        if (nativeSelect1.getSelectedItem().get().equals("GBP")) {
                            user.setGbp(new BigDecimal(textField.getValue()).multiply(DataNBP.getRateEUR().get(DataNBP.getRateEUR().size() - 1)).divide(DataNBP.getRateGBP().get(DataNBP.getRateGBP().size() - 1), 8, RoundingMode.HALF_EVEN));
                            userDAO.save(user);
                        } else if (nativeSelect1.getSelectedItem().get().equals("USD")) {
                            user.setUsd(new BigDecimal(textField.getValue()).multiply(DataNBP.getRateEUR().get(DataNBP.getRateEUR().size() - 1)).divide(DataNBP.getRateUSD().get(DataNBP.getRateUSD().size() - 1), 8, RoundingMode.HALF_EVEN));
                            userDAO.save(user);
                        }
                    } else if (nativeSelect.getSelectedItem().get().equals("USD")) {
                        user.setUsd(user.getUsd().subtract(new BigDecimal(textField.getValue())));
                        if (nativeSelect1.getSelectedItem().get().equals("GBP")) {
                            user.setGbp(new BigDecimal(textField.getValue()).multiply(DataNBP.getRateUSD().get(DataNBP.getRateUSD().size() - 1)).divide(DataNBP.getRateGBP().get(DataNBP.getRateGBP().size() - 1), 8, RoundingMode.HALF_EVEN));
                            userDAO.save(user);
                        } else if (nativeSelect1.getSelectedItem().get().equals("EUR")) {
                            user.setEur(new BigDecimal(textField.getValue()).multiply(DataNBP.getRateUSD().get(DataNBP.getRateUSD().size() - 1)).divide(DataNBP.getRateEUR().get(DataNBP.getRateEUR().size() - 1), 8, RoundingMode.HALF_EVEN));
                            userDAO.save(user);
                        }
                    }
                }

            }
        });

        buttonsRow.addComponents(radioButtonGroup, nativeSelect, textField, label, nativeSelect1, textField1, doIt);


    }
}
