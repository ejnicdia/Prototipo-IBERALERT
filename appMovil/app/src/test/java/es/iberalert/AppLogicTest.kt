package es.iberalert

import org.junit.Test
import org.junit.Assert

class AppLogicTest {
    
    @Test
    fun testLoginSuccess() {
        val viewModel = AppViewModel()
        val success = viewModel.login("admin", "admin")
        Assert.assertTrue(success)
        Assert.assertNotNull(viewModel.currentUser)
        Assert.assertEquals("autoridad", viewModel.currentUser?.role)
    }

    @Test
    fun testLoginFailure() {
        val viewModel = AppViewModel()
        val success = viewModel.login("wrong", "pass")
        Assert.assertFalse(success)
        Assert.assertNull(viewModel.currentUser)
    }

    @Test
    fun testToggleSubscription() {
        val viewModel = AppViewModel()
        viewModel.login("admin", "admin")
        val city = "Madrid, Madrid"
        
        // Initial state
        Assert.assertTrue(viewModel.currentUser?.subscriptions?.contains(city) == true)
        
        // Unsubscribe
        viewModel.toggleSubscription(city)
        Assert.assertFalse(viewModel.currentUser?.subscriptions?.contains(city) == true)
        
        // Subscribe back
        viewModel.toggleSubscription(city)
        Assert.assertTrue(viewModel.currentUser?.subscriptions?.contains(city) == true)
    }

    @Test
    fun testTranslation() {
        val viewModel = AppViewModel()
        viewModel.currentLanguage = "es"
        Assert.assertEquals("Inicio", viewModel.t("nav_home"))
        
        viewModel.currentLanguage = "ca"
        Assert.assertEquals("Inici", viewModel.t("nav_home"))
        
        viewModel.currentLanguage = "eu"
        Assert.assertEquals("Hasiera", viewModel.t("nav_home"))
    }
}
