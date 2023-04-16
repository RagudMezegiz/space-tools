// Copyright 2023 David Terhune
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.github.ragudmezegiz.space.command

/** Factory for making command instances. */
class CommandFactory {

    static Map<String, Command> commands = [:]

    /**
     * Make a command.
     * @param cmdName name of command
     */
    static Command makeCommand(String cmdName) {
        if (commands.containsKey(cmdName)) {
            return commands.get(cmdName)
        }
        throw new IllegalArgumentException("${cmdName} is not a valid command")
    }

}
