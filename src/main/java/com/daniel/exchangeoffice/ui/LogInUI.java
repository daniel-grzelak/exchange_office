package com.daniel.exchangeoffice.ui;

import com.daniel.exchangeoffice.DAO.UserDAO;
import com.daniel.exchangeoffice.classes.User;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;




@SpringUI
public class LogInUI extends UI implements View {
    @Autowired
    private UserDAO userDAO;
    private Binder<User> binder = new Binder<>();
    private final static String regex = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z]).{8,12}$";


    @Override
    protected void init(VaadinRequest vaadinRequest) {

        setContent(createGUI());


    }

    public VerticalLayout createGUI() {
        VerticalLayout verticalLayout = new VerticalLayout();


        verticalLayout.setSizeFull();
        GridLayout grid = new GridLayout(3, 4);

        Label lLogin = new Label("Login: ");
        lLogin.setWidth(String.valueOf(100));
        TextField tfLogin = new TextField();


        HorizontalLayout loginHorizontal = new HorizontalLayout();
        loginHorizontal.addComponents(lLogin, tfLogin);

        grid.addComponent(loginHorizontal, 1, 1);

        Label lPassword = new Label("Password: ");
        lPassword.setWidth(String.valueOf(100));
        PasswordField tfPassword = new PasswordField();
        HorizontalLayout passwordHorizontal = new HorizontalLayout();
        passwordHorizontal.addComponents(lPassword, tfPassword);

        grid.addComponent(passwordHorizontal, 1, 2);

        Button bLogIn = new Button("Log-in");
        grid.addComponent(bLogIn, 1, 3);

        verticalLayout.addComponents(grid);
        verticalLayout.setComponentAlignment(grid, Alignment.MIDDLE_CENTER);

        bLogIn.addClickListener(event -> {
            if (binder.isValid()) {

                User u = new User(tfLogin.getValue(), tfPassword.getValue());
                User uFromDatabase = userDAO.findByUsername(u.getUsername());


                if (u.getPassword().equals(uFromDatabase.getPassword())) {
                    getPage().setTitle("Exchange office");
                    Navigator navigator = new Navigator(this, this);
                    navigator.addView("", new MyUI(userDAO));
                    getUI().getNavigator().navigateTo("");
                    getUI().getSession().setAttribute("User", uFromDatabase);


                } else {
                    Notification note = new Notification("There is no such user", "Please provide with the correct username or password", Notification.Type.WARNING_MESSAGE);
                    note.show(Page.getCurrent());
                }


            } else {
                Notification note = new Notification("Wrong username or password", "Please provide with the correct username or password", Notification.Type.WARNING_MESSAGE);
                note.show(Page.getCurrent());
            }


        });

        binder.forField(tfLogin).withValidator(new StringLengthValidator("Your username must be between 2-12 characters.", 2, 12)).asRequired("You must provide username").bind(User::getUsername, User::setUsername);

        binder.forField(tfPassword).withValidator((Validator<String>) (s, valueContext) -> {

            if (s.matches(regex)) {

                return ValidationResult.ok();

            } else {
                return ValidationResult.error("Your password must be 8-12 characters and  contain at least one uppercase and one lowercase letter.");
            }
        }).asRequired("You must provide password.").bind(User::getPassword, User::setPassword);


        return verticalLayout;
    }

}

