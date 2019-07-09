package com.uber.rib.core;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.uber.autodispose.AutoDispose;
import com.uber.rib.core.navigation.Navigation;
import com.uber.rib.core.navigation.Node;
import com.uber.rib.core.navigation.NodeManager;
import dagger.Lazy;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public abstract class NavigationInteractor<P, R extends Router> extends Interactor<P, R> implements Node {

    @NonNull
    private final Lazy<Navigation> navigationUtil;

    @NonNull
    private final String backStackName;

    @NonNull
    private final String nodeName;

    @NonNull
    private final BehaviorSubject<Optional<Bundle>> subjectBundle = BehaviorSubject.create();

    @NonNull
    private final BehaviorSubject<NavData> subjectObject = BehaviorSubject.create();

    @Nullable
    private NodeManager nodeManager;

    public NavigationInteractor(
            @NonNull Lazy<P> presenter,
            @NonNull Lazy<R> router,
            @NonNull Lazy<Navigation> navigationUtil,
            @NonNull String backStackName,
            @NonNull String nodeName
    ) {
        super(presenter, router);

        this.navigationUtil = navigationUtil;
        this.backStackName = backStackName;
        this.nodeName = nodeName;
    }

    public NavigationInteractor(
            @NonNull Lazy<P> presenter,
            @NonNull Lazy<R> router,
            @NonNull Lazy<Navigation> navigationUtil,
            @NonNull String backStackName
    ) {
        this(presenter, router, navigationUtil, backStackName, NavigationInteractor.class.getSimpleName());
    }

    @NonNull
    public NodeManager getNavigation() {
        if (nodeManager == null) {
            NodeManager manager = navigationUtil
                    .get().getNodeManager(backStackName);

            nodeManager = manager;

            return manager;
        } else {
            return nodeManager;
        }
    }

    @Override
    protected final void didBecomeActive(@Nullable Bundle savedInstanceState) {
        super.didBecomeActive(savedInstanceState);

        getNavigation().addNode(nodeName, this);

        AutoDispose.autoDisposable(this)
                .apply(Observable.combineLatest(subjectBundle, subjectObject,
                        (op1, navData) -> new Data(navData, op1.orNull())))
                .subscribe((d) -> {
                    Data data = (Data) d;
                    didBecomeActive(data.savedInstanceState,
                            data.navData.child, data.navData.data);
                });

        subjectBundle.onNext(Optional.fromNullable(savedInstanceState));
    }

    @Override
    public final void onNavigation(@Nullable String child, @Nullable Object data) {
        subjectObject.onNext(new NavData(child, data));
    }

    protected void didBecomeActive(
            @Nullable Bundle savedInstanceState,
            @Nullable String child,
            @Nullable Object data
    ) { }

    @Override
    protected void willResignActive() {
        getNavigation().removeNode(nodeName);
        super.willResignActive();
    }

    private static class NavData {

        @Nullable
        private final String child;

        @Nullable
        private final Object data;

        NavData(@Nullable String child, @Nullable Object data) {
            this.child = child;
            this.data = data;
        }

        @Nullable
        public String getChild() {
            return child;
        }

        @Nullable
        public Object getData() {
            return data;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            NavData navData = (NavData) o;

            if (child != null ? !child.equals(navData.child) : navData.child != null) return false;
            return data != null ? data.equals(navData.data) : navData.data == null;
        }

        @Override
        public int hashCode() {
            int result = child != null ? child.hashCode() : 0;
            result = 31 * result + (data != null ? data.hashCode() : 0);
            return result;
        }
    }

    private static class Data {

        @NonNull
        private final NavData navData;

        @Nullable
        private final Bundle savedInstanceState;

        Data(@NonNull NavData navData, @Nullable Bundle savedInstanceState) {
            this.navData = navData;
            this.savedInstanceState = savedInstanceState;
        }

        @NonNull
        public NavData getNavData() {
            return navData;
        }

        @Nullable
        public Bundle getSavedInstanceState() {
            return savedInstanceState;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Data data = (Data) o;

            if (!navData.equals(data.navData)) return false;
            return savedInstanceState != null ? savedInstanceState.equals(
                    data.savedInstanceState) : data.savedInstanceState == null;
        }

        @Override
        public int hashCode() {
            int result = navData.hashCode();
            result = 31 * result + (savedInstanceState != null ? savedInstanceState.hashCode() : 0);
            return result;
        }
    }
}
