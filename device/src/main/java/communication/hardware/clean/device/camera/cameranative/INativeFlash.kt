package communication.hardware.clean.device.camera.cameranative

interface INativeFlash {
    fun mode(mode: String)
    fun mode(): String
}