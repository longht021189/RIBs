package library.rxux.core

import org.junit.Assert
import org.junit.Test

class RouterTest {

    @Test
    fun testChildFlow() {
        val parent = Router()
        val child1 = Router().apply { parent.addChild(this) }
        val child2 = Router().apply { parent.addChild(this) }
        val child3 = Router().apply { child1.addChild(this) }
        val child4 = Router().apply { child2.addChild(this) }

        Assert.assertEquals("Parent is not Attached.", RouterState.Detached, parent.state)
        Assert.assertEquals("Child1 is not Attached.", RouterState.Detached, child1.state)
        Assert.assertEquals("Child2 is not Attached.", RouterState.Detached, child2.state)
        Assert.assertEquals("Child3 is not Attached.", RouterState.Detached, child3.state)
        Assert.assertEquals("Child4 is not Attached.", RouterState.Detached, child4.state)

        parent.dispatchAttach()

        Assert.assertEquals("Parent is Attached.", RouterState.Attached, parent.state)
        Assert.assertEquals("Child1 is Attached.", RouterState.Attached, child1.state)
        Assert.assertEquals("Child2 is Attached.", RouterState.Attached, child2.state)
        Assert.assertEquals("Child3 is Attached.", RouterState.Attached, child3.state)
        Assert.assertEquals("Child4 is Attached.", RouterState.Attached, child4.state)

        val child5 = Router().apply { child4.addChild(this) }

        Assert.assertEquals("child5 is Attached.", RouterState.Attached, child5.state)

        parent.dispatchDetach()

        Assert.assertEquals("Parent is Detached.", RouterState.Detached, parent.state)
        Assert.assertEquals("Child1 is Detached.", RouterState.Detached, child1.state)
        Assert.assertEquals("Child2 is Detached.", RouterState.Detached, child2.state)
        Assert.assertEquals("Child3 is Detached.", RouterState.Detached, child3.state)
        Assert.assertEquals("Child4 is Detached.", RouterState.Detached, child4.state)
        Assert.assertEquals("Child5 is Detached.", RouterState.Detached, child5.state)
    }

    @Test
    fun testAutoDispose() {

    }
}