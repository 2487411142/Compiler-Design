#include "minic-stdlib.h"
struct Parent {
int name;
};
struct Student {
    void id;
    struct Parent p;
};
struct Child {
    struct Parent p;
    int a;
};

struct Student s;
struct Child c;

void sem(int j){

}

int main(int j){
    char c;
    sem(c);
}