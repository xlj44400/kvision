package test.pl.treksoft.kvision.form

import pl.treksoft.kvision.form.Form
import pl.treksoft.kvision.form.text.Text
import pl.treksoft.kvision.form.time.DateTime
import test.pl.treksoft.kvision.SimpleSpec
import kotlin.js.Date
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@Suppress("CanBeParameter")
class FormSpec : SimpleSpec {

    @Test
    fun add() {
        run {
            class DataForm(val map: Map<String, Any?>) {
                val a: String? by map
                val b: Boolean? by map
                val c: Date? by map
            }

            val form = Form {
                DataForm(it)
            }
            val data = DataForm(mapOf("a" to "Test value"))
            form.setData(data)
            val result = form.getData()
            assertNull(result.a, "Form should return null without adding any control")
            val textField = Text()
            form.add("a", textField)
            form.setData(data)
            val result2 = form.getData()
            assertEquals("Test value", result2.a, "Form should return initial value")
        }
    }

    @Test
    fun remove() {
        run {
            class DataForm(val map: Map<String, Any?>) {
                val a: String? by map
                val b: Boolean? by map
                val c: Date? by map
            }

            val form = Form {
                DataForm(it)
            }
            val data = DataForm(mapOf("a" to "Test value"))
            form.add("a", Text())
            form.setData(data)
            form.remove("a")
            val result = form.getData()
            assertNull(result.a, "Form should return null after removing control")
        }
    }

    @Test
    fun removeAll() {
        run {
            class DataForm(val map: Map<String, Any?>) {
                val a: String? by map
                val b: Boolean? by map
                val c: Date? by map
            }

            val form = Form {
                DataForm(it)
            }
            val data = DataForm(mapOf("a" to "Test value"))
            form.add("a", Text())
            form.setData(data)
            form.removeAll()
            val result = form.getData()
            assertNull(result.a, "Form should return null after removing all controls")
        }
    }

    @Test
    fun getControl() {
        run {
            class DataForm(val map: Map<String, Any?>) {
                val a: String? by map
                val b: Boolean? by map
                val c: Date? by map
            }

            val form = Form {
                DataForm(it)
            }
            form.add("a", Text())
            val control = form.getControl("b")
            assertNull(control, "Should return null when there is no such control")
            val control2 = form.getControl("a")
            assertNotNull(control2, "Should return correct control")
        }
    }

    @Test
    fun get() {
        run {
            class DataForm(val map: Map<String, Any?>) {
                val a: String? by map
                val b: Boolean? by map
                val c: Date? by map
            }

            val form = Form {
                DataForm(it)
            }
            val data = DataForm(mapOf("a" to "Test value"))
            form.add("a", Text())
            val b = form["b"]
            assertNull(b, "Should return null value when there is no added control")
            val a = form["a"]
            assertNull(a, "Should return null value when control is empty")
            form.setData(data)
            val a2 = form["a"]
            assertEquals("Test value", a2, "Should return correct value")
        }
    }

    @Test
    fun getData() {
        run {
            class DataForm(val map: Map<String, Any?>) {
                val a: String? by map
                val b: Boolean? by map
                val c: Date? by map
            }

            val form = Form {
                DataForm(it)
            }
            val data = DataForm(mapOf("a" to "Test value"))
            val textField = Text()
            form.add("a", textField)
            form.setData(data)
            textField.value = "New value"
            val result = form.getData()
            assertEquals("New value", result.a, "Form should return changed value")
        }
    }

    @Test
    fun validate() {
        run {
            class DataForm(val map: Map<String, Any?>) {
                val s: String? by map
                val d: Date? by map
            }

            val form = Form {
                DataForm(it)
            }
            form.add("s", Text()) {
                it.getValue()?.length ?: 0 > 4
            }
            form.add("d", DateTime(), required = true)
            form.setData(DataForm(mapOf("s" to "123")))
            val valid = form.validate()
            assertEquals(false, valid, "Should be invalid with initial data")
            form.setData(DataForm(mapOf("s" to "12345")))
            val valid2 = form.validate()
            assertEquals(false, valid2, "Should be invalid with partially changed data")
            form.setData(DataForm(mapOf("s" to "12345", "d" to Date())))
            val valid3 = form.validate()
            assertEquals(true, valid3, "Should be valid")
        }
    }

}