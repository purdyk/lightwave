package lightwave

import lightwave.api.nanoleaf.NanoleafClient
import lightwave.api.nanoleaf.rendering.*
import lightwave.api.nanoleaf.rendering.animation.Animator.Companion.animate
import lightwave.api.nanoleaf.rendering.colors.ColorRGB
import lightwave.api.nanoleaf.rendering.effects.*

class Server {

  private val host = System.getenv("LIGHTWAVE_HOST")
  private val auth = System.getenv("LIGHTWAVE_AUTH")

  private val nClient = NanoleafClient(host, auth)

  fun run() {
    println("Server running.")
    nClient.info()?.let { stat ->
      val bright = stat.state.brightness.value
      val program = stat.effects.select
      val isOn = stat.state.on.value

      nClient.power(true)
      nClient.brightness(stat.state.brightness.max)

      nClient.stream()?.let { stream ->

        val white = ColorRGB(255, 255, 255)

        animate(20) {
          together {

            always {
              FillEffect(stat.panelLayout, ColorRGB(0,0,0))
            }

            inSequence {
              through(2.0) {
                PointEffect(
                  stat.panelLayout, LinearEffect.Direction.Down,
                  white
                )
              }

              through(2.0) {
                ProgressEffect(
                  stat.panelLayout, LinearEffect.Direction.Up,
                  white
                )
              }

              through(2.0) {
                PointEffect(
                  stat.panelLayout, LinearEffect.Direction.Down
                )
              }

              through(2.0) {
                ProgressEffect(
                  stat.panelLayout, LinearEffect.Direction.Up
                )
              }

              together {
                through(10.0) {
                  BlendEffect(stat.panelLayout, LinearEffect.Direction.Up)
                }
                through(2.0) {
                  PointEffect(
                    stat.panelLayout, LinearEffect.Direction.Down,
                    ColorRGB(0,200,128)
                  )
                }
                through(2.0) {
                  PointEffect(
                    stat.panelLayout, LinearEffect.Direction.Up,
                    ColorRGB(255,200,0)
                  )
                }
              }
            }
          }

        }.into(stream)

      }

      nClient.brightness(bright)
      nClient.effect(program)

      nClient.power(isOn)
    }
  }
}