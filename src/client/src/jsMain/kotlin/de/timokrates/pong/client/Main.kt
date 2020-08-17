package de.timokrates.pong.client

import kotlinx.browser.document
import kotlinx.html.dom.append
import kotlinx.html.js.canvas

fun main() {
    document.getElementById("app")?.apply{
        innerHTML = ""
        append {
            canvas {
                id = "canvas"
            }
        }
    }
}
