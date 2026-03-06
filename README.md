Name: Kenning Spath (jct9dr)

A few notes:
- A (W) signifies the winning team
- Sometimes there is no winner for a final game. This is an issue with the API itself, not my project
- The calendar modal input sometimes glitches a bit when transitioning from text input to picker input. This is an emulator issue. I am using a built-in component to Material3, so this is not something I did incorrectly
- Finally, if the API somehow sent data missing keys, my app would simply fail to fetch those games gracefully. I made the assumption that no json keys will be omitted (though my code can recover if some of them are and still fetch those games just to program defensively)

References:

- Android and Kotlin Docs
- Gemini 3 Pro for help with architectural design patterns, for debugging, as an API reference, and code generation (cited in the code). Gemini also helped me with best practices for intents, flows, viewmodels, room, and retrofit
- [Google Mars code lab](https://developer.android.com/codelabs/basic-android-kotlin-compose-getting-data-internet#4)
- My previous homeworks
- Counters lab and in-class examples
- Everything else is cited in-line and all generated code is cited in-line

[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/NYuLn2p4)
