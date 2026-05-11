# Unix Shell

A minimal Unix shell written in C, built around low-level process and system-call behavior. It supports command execution, argument parsing, built-in commands, `fork` / `exec`, synchronous child waiting, and a custom tokenizer for dynamic user input.

I like this project because it is very close to the operating system. It forced me to reason directly about processes, control flow, and how a shell actually dispatches work instead of treating that machinery as a black box.

## Highlights

-   Interactive command loop from read to tokenize to execute
-   Built-in commands including `cd` and `exit`
-   Process creation with `fork()` and external command execution with `execvp()`
-   Child synchronization with `waitpid()`
-   Custom tokenization and linked-list-backed argument handling

[View on GitHub](https://github.com/caroline-hughes/unixShell#readme)
