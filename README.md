В проекте представлен custom view аналоговых часов.
для подключения в проект:
implementation(project(":clock_custom_view_lib"))
Визуально:
<img width="312" alt="image" src="https://github.com/MaryoneFrolova/AnalogClockView/assets/35322384/d3d567a5-021b-4a5e-ab7c-0cbe201203c7">
Без дополнительных атрибутов.
При добавлении customView в проект вызывать у него методы customView.startClock() и customView.stopClock() для запуска и останова часов.
