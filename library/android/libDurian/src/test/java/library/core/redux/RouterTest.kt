package library.core.redux

import org.junit.Assert
import org.junit.Test
import library.core.redux.Reducer as Base

class RouterTest {

    private class Router : Base<Int, Int>()

    @Test
    fun testChildFlow() {
        val parent = Router()
        val child1 = Router().apply { parent.addChild(this) }
        val child2 = Router().apply { parent.addChild(this) }
        val child3 = Router().apply { child1.addChild(this) }
        val child4 = Router().apply { child2.addChild(this) }

        Assert.assertEquals("Parent is not Attached.", ReducerState.Detached, parent.routerState)
        Assert.assertEquals("Child1 is not Attached.", ReducerState.Detached, child1.routerState)
        Assert.assertEquals("Child2 is not Attached.", ReducerState.Detached, child2.routerState)
        Assert.assertEquals("Child3 is not Attached.", ReducerState.Detached, child3.routerState)
        Assert.assertEquals("Child4 is not Attached.", ReducerState.Detached, child4.routerState)

        parent.dispatchAttach()

        Assert.assertEquals("Parent is Attached.", ReducerState.Attached, parent.routerState)
        Assert.assertEquals("Child1 is Attached.", ReducerState.Attached, child1.routerState)
        Assert.assertEquals("Child2 is Attached.", ReducerState.Attached, child2.routerState)
        Assert.assertEquals("Child3 is Attached.", ReducerState.Attached, child3.routerState)
        Assert.assertEquals("Child4 is Attached.", ReducerState.Attached, child4.routerState)

        val child5 = Router().apply { child4.addChild(this) }

        Assert.assertEquals("child5 is Attached.", ReducerState.Attached, child5.routerState)

        parent.dispatchDetach()

        Assert.assertEquals("Parent is Detached.", ReducerState.Detached, parent.routerState)
        Assert.assertEquals("Child1 is Detached.", ReducerState.Detached, child1.routerState)
        Assert.assertEquals("Child2 is Detached.", ReducerState.Detached, child2.routerState)
        Assert.assertEquals("Child3 is Detached.", ReducerState.Detached, child3.routerState)
        Assert.assertEquals("Child4 is Detached.", ReducerState.Detached, child4.routerState)
        Assert.assertEquals("Child5 is Detached.", ReducerState.Detached, child5.routerState)

        parent.dispatchAttach()

        Assert.assertEquals("Parent is Attached.", ReducerState.Attached, parent.routerState)
        Assert.assertEquals("Child1 is Attached.", ReducerState.Attached, child1.routerState)
        Assert.assertEquals("Child2 is Attached.", ReducerState.Attached, child2.routerState)
        Assert.assertEquals("Child3 is Attached.", ReducerState.Attached, child3.routerState)
        Assert.assertEquals("Child4 is Attached.", ReducerState.Attached, child4.routerState)
        Assert.assertEquals("Child5 is Attached.", ReducerState.Attached, child5.routerState)

        parent.dispatchDetach()

        Assert.assertEquals("Parent is Detached.", ReducerState.Detached, parent.routerState)
        Assert.assertEquals("Child1 is Detached.", ReducerState.Detached, child1.routerState)
        Assert.assertEquals("Child2 is Detached.", ReducerState.Detached, child2.routerState)
        Assert.assertEquals("Child3 is Detached.", ReducerState.Detached, child3.routerState)
        Assert.assertEquals("Child4 is Detached.", ReducerState.Detached, child4.routerState)
        Assert.assertEquals("Child5 is Detached.", ReducerState.Detached, child5.routerState)
    }
}