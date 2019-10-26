package library.rxux.core

import io.reactivex.subjects.PublishSubject
import org.junit.Assert
import org.junit.Test
import library.rxux.core.Router as Base

class RouterTest {

    private class Router : Base<Int, Int>(
        initialState = 0,
        actionSubject = PublishSubject.create(),
        stateSubject = PublishSubject.create()
    ) {
        override fun onUpdate(oldState: Int, action: Int): Int {
            return action
        }
    }

    @Test
    fun testChildFlow() {
        val parent = Router()
        val child1 = Router().apply { parent.addChild(this) }
        val child2 = Router().apply { parent.addChild(this) }
        val child3 = Router().apply { child1.addChild(this) }
        val child4 = Router().apply { child2.addChild(this) }

        Assert.assertEquals("Parent is not Attached.", RouterState.Detached, parent.routerState)
        Assert.assertEquals("Child1 is not Attached.", RouterState.Detached, child1.routerState)
        Assert.assertEquals("Child2 is not Attached.", RouterState.Detached, child2.routerState)
        Assert.assertEquals("Child3 is not Attached.", RouterState.Detached, child3.routerState)
        Assert.assertEquals("Child4 is not Attached.", RouterState.Detached, child4.routerState)

        parent.dispatchAttach()

        Assert.assertEquals("Parent is Attached.", RouterState.Attached, parent.routerState)
        Assert.assertEquals("Child1 is Attached.", RouterState.Attached, child1.routerState)
        Assert.assertEquals("Child2 is Attached.", RouterState.Attached, child2.routerState)
        Assert.assertEquals("Child3 is Attached.", RouterState.Attached, child3.routerState)
        Assert.assertEquals("Child4 is Attached.", RouterState.Attached, child4.routerState)

        val child5 = Router().apply { child4.addChild(this) }

        Assert.assertEquals("child5 is Attached.", RouterState.Attached, child5.routerState)

        parent.dispatchDetach()

        Assert.assertEquals("Parent is Detached.", RouterState.Detached, parent.routerState)
        Assert.assertEquals("Child1 is Detached.", RouterState.Detached, child1.routerState)
        Assert.assertEquals("Child2 is Detached.", RouterState.Detached, child2.routerState)
        Assert.assertEquals("Child3 is Detached.", RouterState.Detached, child3.routerState)
        Assert.assertEquals("Child4 is Detached.", RouterState.Detached, child4.routerState)
        Assert.assertEquals("Child5 is Detached.", RouterState.Detached, child5.routerState)

        parent.dispatchAttach()

        Assert.assertEquals("Parent is Attached.", RouterState.Attached, parent.routerState)
        Assert.assertEquals("Child1 is Attached.", RouterState.Attached, child1.routerState)
        Assert.assertEquals("Child2 is Attached.", RouterState.Attached, child2.routerState)
        Assert.assertEquals("Child3 is Attached.", RouterState.Attached, child3.routerState)
        Assert.assertEquals("Child4 is Attached.", RouterState.Attached, child4.routerState)
        Assert.assertEquals("Child5 is Attached.", RouterState.Attached, child5.routerState)

        parent.dispatchDetach()

        Assert.assertEquals("Parent is Detached.", RouterState.Detached, parent.routerState)
        Assert.assertEquals("Child1 is Detached.", RouterState.Detached, child1.routerState)
        Assert.assertEquals("Child2 is Detached.", RouterState.Detached, child2.routerState)
        Assert.assertEquals("Child3 is Detached.", RouterState.Detached, child3.routerState)
        Assert.assertEquals("Child4 is Detached.", RouterState.Detached, child4.routerState)
        Assert.assertEquals("Child5 is Detached.", RouterState.Detached, child5.routerState)
    }

    @Test
    fun testActionAndState() {
        val router = Router()

    }
}