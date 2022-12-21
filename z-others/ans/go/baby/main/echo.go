package main

import "fmt"

func main() {
	var index = 0

	for i := index; i < 6667; i++ {
		if i == 0 {
			fmt.Println("------start------")
		}

		fmt.Print(i)
		fmt.Println(": halo, Go!")

		if i == 6666 {
			fmt.Println("------end------")
		}
	}
}
