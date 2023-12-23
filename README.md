# Weather service

Integrates existing (legacy) weather REST API into our system. The chosen API
is [OpenWeatherMap](https://openweathermap.org/) because it provides a free tier, and it categorizes weather conditions
which makes it easier to check for harsh or hazardous weather.

The idea is to listen for order events, and when an order is placed, we check the weather for the delivery address. If
the weather is bad, we emit an event describing the weather conditions. This event can be picked up by the notification
service, which can then notify the customer that the weather is bad and that the delivery might be delayed, and to
remember to tip their delivery person.
