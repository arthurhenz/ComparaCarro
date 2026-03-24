package com.common.navigation

data class NavOptions(
    val popUpTo: Any? = null,              // Remove da pilha até chegar nessa rota
    val popUpToInclusive: Boolean = false, // Se true, remove a própria rota alvo também
    val singleTop: Boolean = false,        // Evita criar duplicata se a rota já está no topo
    val replaceTop: Boolean = false        // Substitui a rota atual no topo da pilha
)
