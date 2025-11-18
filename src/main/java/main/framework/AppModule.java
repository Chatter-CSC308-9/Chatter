package main.framework;

import javafx.util.Callback;
import main.boundaries.Boundary;
import main.boundaries.Shell;
import main.boundaries.screens.*;
import main.controllers.*;
import main.controllers.apis.UserIdApiController;

import java.util.*;
import java.util.function.Supplier;

final class AppModule {

    // Keep singletons by concrete class
    private final Map<Class<?>, Object> singletons = new HashMap<>();

    // Boundaries are created from suppliers (so we can inject controllers easily)
    private final Map<Class<? extends Boundary>, Supplier<? extends Boundary>> boundarySuppliers = new HashMap<>();

    AppModule() {
        // --- Controllers (singletons) ---
        register(new LoginController());
        register(new EditProjectController());
        register(new UserIdApiController());
        register(new LogoutController());
        register(new DisplayUsernameController());
        register(new AcceptPaymentController());
        register(new SubmitProjectController());
        register(new ClaimUngradedProjectController());
        register(new SubmitGradedProjectController());
        register(new DownloadGradedProjectController());
        register(new CreateAccountController());

        // Inject API into all controllers (except the API controller itself)
        var api = get(UserIdApiController.class);
        singletons.values().stream()
                .filter(c -> c instanceof Controller && !(c instanceof UserIdApiController))
                .map(Controller.class::cast)
                .forEach(api::injectControllerAPIs);

        // --- Boundaries (lazy suppliers) ---
        boundarySuppliers.put(Login.class, () -> new Login(get(LoginController.class)));
        boundarySuppliers.put(Current.class, () -> new Current(get(EditProjectController.class)));
        boundarySuppliers.put(CurrentEdit.class, () -> new CurrentEdit(get(EditProjectController.class), get(SubmitProjectController.class)));
        boundarySuppliers.put(Pending.class, () -> new Pending(get(SubmitProjectController.class)));
        boundarySuppliers.put(Account.class, () -> new Account(get(LogoutController.class), get(DisplayUsernameController.class)));
        boundarySuppliers.put(GraderAccount.class, () -> new GraderAccount(get(LogoutController.class), get(DisplayUsernameController.class)));
        boundarySuppliers.put(GraderCatalog.class, () -> new GraderCatalog(get(ClaimUngradedProjectController.class)));
        boundarySuppliers.put(GraderClaimed.class, () -> new GraderClaimed(get(SubmitGradedProjectController.class)));
        boundarySuppliers.put(Graded.class, () -> new Graded(get(DownloadGradedProjectController.class), get(AcceptPaymentController.class)));
        boundarySuppliers.put(CreateAccount.class, () -> new CreateAccount(get(CreateAccountController.class)));
    }

    private <T> void register(T instance) {
        singletons.put(instance.getClass(), instance);
    }

    @SuppressWarnings("unchecked")
    <T> T get(Class<T> type) {
        var obj = singletons.get(type);
        if (obj == null) throw new IllegalStateException("No singleton for " + type.getName());
        return (T) obj;
    }

    Map<Class<? extends Boundary>, Boundary> boundaryInstances() {
        // materialize once (all current boundaries) and return an immutable view
        Map<Class<? extends Boundary>, Boundary> map = new HashMap<>();
        boundarySuppliers.forEach((k, sup) -> map.put(k, sup.get()));
        return Collections.unmodifiableMap(map);
    }

    Callback<Class<?>, Object> controllerFactory() {
        return requestedType -> {
            if (requestedType == Shell.class) {
                var map = (Map<Class<?>, Object>) (Map<?, ?>) boundaryInstances();
                return new Shell(map, get(UserIdApiController.class));
            }
            Object existing = singletons.get(requestedType);
            if (existing != null) return existing;
            throw new IllegalArgumentException("No provider for " + requestedType.getName());
        };
    }
}
