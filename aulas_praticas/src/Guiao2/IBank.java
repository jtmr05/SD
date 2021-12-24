package guiao2;

interface IBank {

    public int balance(int id);

    public boolean deposit(int id, int value);

    public boolean withdraw(int id, int value);

    public boolean transfer(int from, int to, int value);

    public int totalBalance();
}
