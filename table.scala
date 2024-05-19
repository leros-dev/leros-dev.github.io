@main def hello() = {
    val table = Seq(
Seq("Opcode", "Function", "Description"),
Seq("add", "A = A + Rn", "Add register Rn to A"),
Seq("addi", "A = A + i", "Add immediate value i to A (sign extend i)"),
Seq("sub", "A = A - Rn", "Subtract register Rn from A"),
Seq("subi", "A = A - i", "Subtract immediate value i from A (sign extend i)"),
Seq("sra", "A = A >> 1", "Shift A arithmetic right"),
Seq("and", "A = A and Rn", "And register Rn with A"),
Seq("andi", "A = A and i", "And immediate value i with A"),
Seq("or", "A = A or Rn", "Or register Rn with A"),
Seq("ori", "A = A or i", "Or immediate value i with A"),
Seq("xor", "A = A xor Rn", "Xor register Rn with A"),
Seq("xori", "A = A xor i", "Xor immediate value i with A"),
Seq("load", "A = Rn", "Load register Rn into A"),
Seq("loadi", "A = i", "Load immediate value i into A (sign extend i)"),
Seq("loadhi", "A$_{31-8}$ = i", "Load immediate into second byte (sign extend i)"),
Seq("loadh2i", "A$_{31-16}$ = i ", "Load immediate into third byte (sign extend i)"),
Seq("loadh3i", "A$_{31-24}$ = i", "Load immediate into fourth byte (sign extend i)"),
Seq("store", "Rn = A", "Store A into register Rn"),
Seq("jal", "PC = A, Rn = PC + 2", "Jump to A and store return address in Rn"),
Seq("ldaddr", "AR = A", "Load address register AR with A"),
Seq("loadind", "A = mem[AR+(i << 2)]", "Load a word from memory into A"),
Seq("loadindb", "A = mem[AR+i]$_{7-0}$", "Load a byte signe extending from memory into A"),
Seq("storeind", "mem[AR+(i << 2)] = A", "Store A into memory"),
Seq("storeindb", "mem[AR+i]$_{7-0}$ = A", "Store a byte into memory"),
Seq("br", "PC = PC + o", "Branch"),
Seq("brz", "if A == 0 PC = PC + o", "Branch if A is zero"),
Seq("brnz", "if A != 0 PC = PC + o ", "Branch if A is not zero"),
Seq("brp", "if A >= 0 PC = PC + o", "Branch if A is positive"),
Seq("brn", "if A < 0 PC = PC + o", "Branch if A is negative"),
Seq("scall", "scall A", "System call (simulation hook)"),
    )

    // this is ugly
    var l1 = List("")
    var l2 = List("")
    var l3 = List("")

    for (row <- table) {
        l1 = l1.appended(row(0))
        l2 = l2.appended(row(1))
        l3 = l3.appended(row(2))
    }

    val max = Array(l1.map(_.length).max, l2.map(_.length).max, l3.map(_.length).max)

    for (row <- table) {
        for (i <- 0 until row.length) {
            print("| ")
            print(row(i))
            print(" " * (max(i) - row(i).length + 1))
        }
        println("|")
        if (row == table(0)) {
            for (i <- 0 until row.length) {
                print("| ")
                print("-" * (max(i)) + " ")
            }
            println("|")
        }
    }
}