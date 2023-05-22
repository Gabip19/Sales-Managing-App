package salesapp.controller;

import salesapp.domain.User;
import salesapp.service.Service;

public abstract class MainWindowController {
    protected static Service srv;
    protected static User currentUser;

    public static void setSrv(Service srv) {
        MainWindowController.srv = srv;
    }

    public static void setCurrentUser(User currentUser) {
        MainWindowController.currentUser = currentUser;
    }
}
