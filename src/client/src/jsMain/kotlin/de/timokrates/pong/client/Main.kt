package de.timokrates.pong.client

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement


val canvas = initalizeCanvas()
fun initalizeCanvas(): HTMLCanvasElement {
    val canvas = document.createElement("canvas") as HTMLCanvasElement
    val context = canvas.getContext("2d") as CanvasRenderingContext2D
    context.canvas.width = window.innerWidth
    context.canvas.height = window.innerHeight
    window.addEventListener("resize", {
        context.canvas.width = window.innerWidth
        context.canvas.height = window.innerHeight
    })
    document.body!!.appendChild(canvas)
    return canvas
}

suspend fun main() {
    val context = canvas.getContext("2d") as CanvasRenderingContext2D
    game(drawContext = context)
}
