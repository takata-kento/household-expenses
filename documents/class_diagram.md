# 家計簿アプリ クラス図

```mermaid
classDiagram
    %% エンティティクラス
    class User {
        -UserId id
        -Username username
        -UserGroupId userGroupId
        -List~FinancialAccount~ accounts
        -FinancialAccountId mainAccountId
        -List~MonthlySaving~ savings
        +addAccount(FinancialAccount) void
        +removeAccount(FinancialAccountId) void
        +setMainAccount(FinancialAccountId) void
        +hasAccount(FinancialAccountId) boolean
        +getAccountCount() int
        +canRemoveAccount(FinancialAccountId) boolean
        +ensureMainAccountConsistency() void
        +recordMonthlySaving(Year, Month, Money, FinancialAccountId, Description) void
        +getMonthlySaving(Year, Month) MonthlySaving
        +getSavingsByYear(Year) List~MonthlySaving~
        +updateMonthlySaving(Year, Month, Money, Description) void
        +deleteMonthlySaving(Year, Month) void
    }

    class UserGroup {
        -UserGroupId id
        -GroupName groupName
        -Day monthStartDay
        -UserId createdByUserId
        -List~UserId~ memberIds
        -List~GroupInvitation~ invitations
        -List~LivingExpenseCategory~ expenseCategories
        -List~MonthlyBudget~ monthlyBudgets
        -List~FixedExpenseCategory~ fixedExpenseCategories
        -List~FixedExpenseHistory~ fixedExpenseHistories
        +addMember(UserId) void
        +removeMember(UserId) void
        +inviteUser(UserId, UserId) GroupInvitation
        +processInvitationAcceptance(UserId) void
        +processInvitationRejection(UserId) void
        +canUserLeave(UserId) boolean
        +isUserMember(UserId) boolean
        +getMemberCount() int
        +addExpenseCategory(CategoryName, Description, Boolean) LivingExpenseCategory
        +updateExpenseCategory(LivingExpenseCategoryId, CategoryName, Description) void
        +removeExpenseCategory(LivingExpenseCategoryId) void
        +getExpenseCategory(LivingExpenseCategoryId) LivingExpenseCategory
        +getDefaultCategories() List~LivingExpenseCategory~
        +hasExpenseCategory(LivingExpenseCategoryId) boolean
        +setMonthlyBudget(Year, Month, Money, UserId) MonthlyBudget
        +getMonthlyBudget(Year, Month) MonthlyBudget
        +hasMonthlyBudget(Year, Month) boolean
        +addFixedExpenseCategory(CategoryName, Description, Money) FixedExpenseCategory
        +removeFixedExpenseCategory(FixedExpenseCategoryId) void
        +getFixedExpenseCategory(FixedExpenseCategoryId) FixedExpenseCategory
        +addFixedExpenseHistory(FixedExpenseCategoryId, Year, Month, Money, LocalDate, Description) FixedExpenseHistory
        +getFixedExpenseHistories(Year, Month) List~FixedExpenseHistory~
        +getFixedExpenseHistory(FixedExpenseCategoryId, Year, Month) FixedExpenseHistory
        +calculateRemainingBudget(Year, Month, LocalDate, List~DailyLivingExpense~) Money
        +getTotalFixedExpenses(Year, Month) Money
        +ensureFixedExpenseCategoryExists(FixedExpenseCategoryId) boolean
        +isOverBudget(Year, Month, LocalDate, List~DailyLivingExpense~) boolean
    }

    class GroupInvitation {
        -UserGroupId userGroupId
        -UserId invitedUserId
        -UserId invitedByUserId
        -InvitationStatus status
        -LocalDateTime invitedAt
        -LocalDateTime respondedAt
    }

    class FinancialAccount {
        -FinancialAccountId id
        -AccountName accountName
        -Money balance
        -Boolean isMainAccount
        -List~BalanceEditHistory~ editHistories
        +addEditHistory(Money, Money, Description) void
        +getLatestEditHistory() BalanceEditHistory
        +updateBalance(Money, Description) void
    }

    class BalanceEditHistory {
        -BalanceEditHistoryId id
        -Money oldBalance
        -Money newBalance
        -Description editReason
        -LocalDateTime createdAt
    }

    class MonthlyBudget {
        -Year year
        -Month month
        -Money budgetAmount
        -UserId setByUserId
        +updateBudgetAmount(Money, UserId) void
    }

    class LivingExpenseCategory {
        -LivingExpenseCategoryId id
        -CategoryName categoryName
        -Description description
        -Boolean isDefault
    }

    class DailyLivingExpense {
        -DailyLivingExpenseId id
        -UserId userId
        -LocalDate transactionDate
        -LivingExpenseCategoryId livingExpenseCategoryId
        -Money amount
        -Description memo
    }

    class DailyPersonalExpense {
        -UserId userId
        -LocalDate transactionDate
        -SequenceNumber sequenceNo
        -Money amount
        -Description description
    }

    class DailyGroupTransaction {
        -UserGroupId userGroupId
        -LocalDate transactionDate
        -List~DailyLivingExpense~ livingExpenses
        -Money totalLivingExpense
        -Money budgetBalance
        +addLivingExpense(UserId, LivingExpenseCategoryId, Money, Description) void
        +calculateTotalLivingExpense() Money
        +updateBudgetBalance(Money) void
        +getPersonalShareOfLivingExpense(int) Money
    }

    class DailyPersonalTransaction {
        -UserId userId
        -LocalDate transactionDate
        -Money income
        -Money totalExpense
        -List~DailyPersonalExpense~ personalExpenses
        +addPersonalExpense(Money, Description) void
        +calculateTotalExpense(Money) void
        +getTotalPersonalExpense() Money
    }

    class FixedExpenseCategory {
        -FixedExpenseCategoryId id
        -CategoryName categoryName
        -Description description
        -Money defaultAmount
        +updateCategoryName(CategoryName) void
        +updateDescription(Description) void
        +updateDefaultAmount(Money) void
    }

    class FixedExpenseHistory {
        -FixedExpenseHistoryId id
        -FixedExpenseCategoryId categoryId
        -Year year
        -Month month
        -Money amount
        -LocalDate effectiveDate
        -Description memo
    }

    class MonthlySaving {
        -Year year
        -Month month
        -Money savingAmount
        -FinancialAccountId financialAccountId
        -Description memo
    }

    %% リクエストクラス
    class UserRegistrationRequest {
        -Username username
        -String password
        +getUsername() Username
        +getPassword() String
    }

    class UserLoginRequest {
        -Username username
        -String password
        +getUsername() Username
        +getPassword() String
    }

    class DailyTransactionRequest {
        -LocalDate transactionDate
        -Money income
        -List~DailyLivingExpenseRequest~ livingExpenses
        -List~DailyPersonalExpenseRequest~ personalExpenses
        +getTransactionDate() LocalDate
        +getIncome() Money
        +getLivingExpenses() List~DailyLivingExpenseRequest~
        +getPersonalExpenses() List~DailyPersonalExpenseRequest~
    }

    class DailyLivingExpenseRequest {
        -Long categoryId
        -Money amount
        -Description memo
        +getCategoryId() Long
        +getAmount() Money
        +getMemo() Description
    }

    class DailyPersonalExpenseRequest {
        -Money amount
        -Description description
        +getAmount() Money
        +getDescription() Description
    }

    class MonthlyBudgetRequest {
        -Year year
        -Month month
        -Money budgetAmount
        +getYear() Year
        +getMonth() Month
        +getBudgetAmount() Money
    }

    class GroupInvitationRequest {
        -Username invitedUsername
        +getInvitedUsername() Username
    }

    %% リクエスト間の関係性
    DailyTransactionRequest o-- DailyLivingExpenseRequest
    DailyTransactionRequest o-- DailyPersonalExpenseRequest

    %% 値オブジェクトクラス
    class Money {
        <<record>>
        -int amount
        +amount() int
        +add(Money) Money
        +subtract(Money) Money
    }

    class Username {
        <<record>>
        -String value
        +value() String
    }

    class CategoryName {
        <<record>>
        -String value
        +value() String
    }

    class Description {
        <<record>>
        -String value
        +value() String
    }

    class AccountName {
        <<record>>
        -String value
        +value() String
    }

    class UserId {
        <<record>>
        -long value
        +value() long
    }

    class UserGroupId {
        <<record>>
        -long value
        +value() long
    }

    class FinancialAccountId {
        <<record>>
        -long value
        +value() long
    }

    class Day {
        -Integer value
        +getValue() Integer
        +isValid() boolean
        +isValidForMonth(Month) boolean
    }

    class GroupName {
        -String value
        +getValue() String
        +isValid() boolean
        +length() int
    }

    class SequenceNumber {
        -Integer value
        +getValue() Integer
        +isValid() boolean
        +isPositive() boolean
    }

    class LivingExpenseCategoryId {
        -Long value
        +getValue() Long
        +isValid() boolean
    }

    class FixedExpenseCategoryId {
        -Long value
        +getValue() Long
        +isValid() boolean
    }

    class BalanceEditHistoryId {
        -Long value
        +getValue() Long
        +isValid() boolean
    }

    class DailyLivingExpenseId {
        -Long value
        +getValue() Long
        +isValid() boolean
    }

    class FixedExpenseHistoryId {
        -Long value
        +getValue() Long
        +isValid() boolean
    }

    %% Enumクラス
    class InvitationStatus {
        <<enumeration>>
        PENDING
        ACCEPTED
        REJECTED
        +isPending() boolean
        +isAccepted() boolean
        +isRejected() boolean
    }


    %% サービスクラス
    class UserService {
        -UserRepository userRepository
        -GroupInvitationRepository groupInvitationRepository
        -PasswordEncoder passwordEncoder
        +registerUser(UserRegistrationRequest) User
        +authenticateUser(UserLoginRequest) User
        +getCurrentUser() User
        +findByUsername(Username) Optional~User~
        +acceptGroupInvitation(UserId) void
        +rejectGroupInvitation(UserId) void
        +addAccountToUser(UserId, FinancialAccount) void
        +removeAccountFromUser(UserId, FinancialAccountId) void
        +setMainAccount(UserId, FinancialAccountId) void
        +canRemoveAccount(UserId, FinancialAccountId) boolean
    }

    class UserGroupService {
        -UserGroupRepository userGroupRepository
        -GroupInvitationRepository groupInvitationRepository
        -UserRepository userRepository
        +createGroup(GroupName) UserGroup
        +inviteUser(GroupInvitationRequest) GroupInvitation
        +leaveGroup(UserGroupId) void
        +getGroupMembers(UserGroupId) List~User~
    }

    class TransactionService {
        -DailyTransactionRepository dailyTransactionRepository
        -DailyLivingExpenseRepository dailyLivingExpenseRepository
        -DailyPersonalExpenseRepository dailyPersonalExpenseRepository
        -BudgetService budgetService
        +recordDailyTransaction(DailyTransactionRequest) DailyTransaction
        +calculateTotalExpense(List~DailyLivingExpenseRequest~, List~DailyPersonalExpenseRequest~, Integer) BigDecimal
        +getDailyTransaction(Long, LocalDate) DailyTransaction
        +updateDailyTransaction(Long, LocalDate, DailyTransactionRequest) DailyTransaction
        +deleteDailyTransaction(Long, LocalDate) void
    }

    class BudgetService {
        -MonthlyBudgetRepository monthlyBudgetRepository
        +setMonthlyBudget(MonthlyBudgetRequest) MonthlyBudget
        +getMonthlyBudget(Long, Integer, Integer) MonthlyBudget
        +calculateBudgetBalance(Long, LocalDate, BigDecimal) BigDecimal
    }

    class ExpenseService {
        -LivingExpenseCategoryRepository livingExpenseCategoryRepository
        -FixedExpenseCategoryRepository fixedExpenseCategoryRepository
        -FixedExpenseHistoryRepository fixedExpenseHistoryRepository
        +createLivingExpenseCategory(Long, String, String) LivingExpenseCategory
        +updateLivingExpenseCategory(Long, String, String) LivingExpenseCategory
        +deleteLivingExpenseCategory(Long) void
        +createFixedExpenseCategory(Long, String, String, BigDecimal) FixedExpenseCategory
        +setFixedExpenseAmount(Long, Integer, Integer, BigDecimal) FixedExpenseHistory
        +getFixedExpenses(Long, Integer, Integer) List~FixedExpenseHistory~
    }

    class AccountService {
        -FinancialAccountRepository financialAccountRepository
        -BalanceEditHistoryRepository balanceEditHistoryRepository
        -UserRepository userRepository
        +createAccount(UserId, AccountName, Money, Boolean) FinancialAccount
        +updateBalance(FinancialAccountId, Money) FinancialAccount
        +editBalance(FinancialAccountId, Money, Description) FinancialAccount
        +calculateNewBalance(FinancialAccountId, Money, Money, Money, Money) Money
        +getUserAccounts(UserId) List~FinancialAccount~
    }

    class SavingService {
        -UserRepository userRepository
        -MonthlySavingRepository monthlySavingRepository
        +recordMonthlySaving(UserId, Year, Month, Money, FinancialAccountId, Description) MonthlySaving
        +getMonthlySaving(UserId, Year, Month) MonthlySaving
        +updateMonthlySaving(UserId, Year, Month, Money, Description) MonthlySaving
        +deleteMonthlySaving(UserId, Year, Month) void
        +getSavingsByYear(UserId, Year) List~MonthlySaving~
    }

    %% コントローラークラス
    class UserController {
        -UserService userService
        +register(UserRegistrationRequest) ResponseEntity~User~
        +login(UserLoginRequest) ResponseEntity~String~
        +getCurrentUser() ResponseEntity~User~
        +logout() ResponseEntity~Void~
        +acceptGroupInvitation(UserId) ResponseEntity~Void~
        +rejectGroupInvitation(UserId) ResponseEntity~Void~
    }

    class UserGroupController {
        -UserGroupService userGroupService
        +createGroup(GroupName) ResponseEntity~UserGroup~
        +inviteUser(GroupInvitationRequest) ResponseEntity~GroupInvitation~
        +leaveGroup() ResponseEntity~Void~
        +getGroupMembers() ResponseEntity~List~User~~
    }

    class TransactionController {
        -TransactionService transactionService
        +recordTransaction(DailyTransactionRequest) ResponseEntity~DailyTransaction~
        +getTransaction(LocalDate) ResponseEntity~DailyTransaction~
        +updateTransaction(LocalDate, DailyTransactionRequest) ResponseEntity~DailyTransaction~
        +deleteTransaction(LocalDate) ResponseEntity~Void~
    }

    class BudgetController {
        -BudgetService budgetService
        +setMonthlyBudget(MonthlyBudgetRequest) ResponseEntity~MonthlyBudget~
        +getMonthlyBudget(Integer, Integer) ResponseEntity~MonthlyBudget~
    }

    class ExpenseController {
        -ExpenseService expenseService
        +createLivingExpenseCategory(String, String) ResponseEntity~LivingExpenseCategory~
        +updateLivingExpenseCategory(Long, String, String) ResponseEntity~LivingExpenseCategory~
        +deleteLivingExpenseCategory(Long) ResponseEntity~Void~
        +createFixedExpenseCategory(String, String, BigDecimal) ResponseEntity~FixedExpenseCategory~
        +setFixedExpenseAmount(Long, Integer, Integer, BigDecimal) ResponseEntity~FixedExpenseHistory~
        +getFixedExpenses(Integer, Integer) ResponseEntity~List~FixedExpenseHistory~~
    }

    class AccountController {
        -AccountService accountService
        +createAccount(AccountName, Money, Boolean) ResponseEntity~FinancialAccount~
        +updateBalance(FinancialAccountId, Money) ResponseEntity~FinancialAccount~
        +editBalance(FinancialAccountId, Money, Description) ResponseEntity~FinancialAccount~
        +getUserAccounts() ResponseEntity~List~FinancialAccount~~
    }

    class SavingController {
        -SavingService savingService
        +recordMonthlySaving(Year, Month, Money, FinancialAccountId, Description) ResponseEntity~MonthlySaving~
        +getMonthlySaving(Year, Month) ResponseEntity~MonthlySaving~
        +updateMonthlySaving(Year, Month, Money, Description) ResponseEntity~MonthlySaving~
        +deleteMonthlySaving(Year, Month) ResponseEntity~Void~
        +getSavingsByYear(Year) ResponseEntity~List~MonthlySaving~~
    }

    %% リポジトリインターフェース
    class UserRepository {
        <<interface>>
        +findByUserId(UserId) Optional~User~
        +findByUsername(Username) Optional~User~
        +existsByUsername(Username) boolean
    }

    class UserGroupRepository {
        <<interface>>
        +findByCreatedByUserId(UserId) Optional~UserGroup~
    }

    class GroupInvitationRepository {
        <<interface>>
        +findByInvitedUserIdAndStatus(UserId, InvitationStatus) List~GroupInvitation~
        +findByUserGroupIdAndInvitedUserId(UserGroupId, UserId) Optional~GroupInvitation~
    }

    class FinancialAccountRepository {
        <<interface>>
        +findById(FinancialAccountId) Optional~FinancialAccount~
        +save(FinancialAccount) FinancialAccount
        +deleteById(FinancialAccountId) void
    }

    class BalanceEditHistoryRepository {
        <<interface>>
        +findByFinancialAccountIdOrderByIdDesc(Long) List~BalanceEditHistory~
    }

    class MonthlyBudgetRepository {
        <<interface>>
        +findByUserGroupIdAndYearAndMonth(Long, Integer, Integer) Optional~MonthlyBudget~
    }

    class LivingExpenseCategoryRepository {
        <<interface>>
        +findByUserGroupId(Long) List~LivingExpenseCategory~
        +findByUserGroupIdAndIsDefault(Long, Boolean) List~LivingExpenseCategory~
    }

    class DailyTransactionRepository {
        <<interface>>
        +findByUserIdAndTransactionDate(UserId, LocalDate) Optional~DailyTransaction~
        +findByUserIdAndTransactionDateBetween(UserId, LocalDate, LocalDate) List~DailyTransaction~
    }

    class DailyLivingExpenseRepository {
        <<interface>>
        +findByUserIdAndTransactionDate(UserId, LocalDate) List~DailyLivingExpense~
        +findByUserIdAndTransactionDateBetween(UserId, LocalDate, LocalDate) List~DailyLivingExpense~
    }

    class DailyPersonalExpenseRepository {
        <<interface>>
        +findByUserIdAndTransactionDate(UserId, LocalDate) List~DailyPersonalExpense~
        +findByUserIdAndTransactionDateBetween(UserId, LocalDate, LocalDate) List~DailyPersonalExpense~
    }

    class FixedExpenseCategoryRepository {
        <<interface>>
        +findByUserGroupId(Long) List~FixedExpenseCategory~
    }

    class FixedExpenseHistoryRepository {
        <<interface>>
        +findByFixedExpenseCategoryIdAndYearAndMonth(Long, Integer, Integer) Optional~FixedExpenseHistory~
        +findByFixedExpenseCategoryIdInAndYear(List~Long~, Integer) List~FixedExpenseHistory~
    }

    class MonthlySavingRepository {
        <<interface>>
        +findByUserIdAndYearAndMonth(UserId, Year, Month) Optional~MonthlySaving~
        +findByUserIdAndYear(UserId, Year) List~MonthlySaving~
    }

    %% 値オブジェクトとの関係性
    User o-- UserId
    User o-- Username
    User o-- UserGroupId
    User o-- FinancialAccountId
    UserGroup o-- UserGroupId
    UserGroup o-- GroupName
    UserGroup o-- Day
    UserGroup o-- UserId
    GroupInvitation o-- UserGroupId
    GroupInvitation o-- UserId
    GroupInvitation o-- InvitationStatus
    FinancialAccount o-- FinancialAccountId
    FinancialAccount o-- AccountName
    FinancialAccount o-- Money
    BalanceEditHistory o-- BalanceEditHistoryId
    BalanceEditHistory o-- Money
    BalanceEditHistory o-- Description
    MonthlySaving o-- FinancialAccountId
    DailyGroupTransaction o-- UserGroupId
    DailyPersonalTransaction o-- UserId
    MonthlyBudget o-- UserGroupId
    MonthlyBudget o-- UserId
    MonthlyBudget o-- Year
    MonthlyBudget o-- Month
    MonthlyBudget o-- Money
    LivingExpenseCategory o-- LivingExpenseCategoryId
    LivingExpenseCategory o-- CategoryName
    LivingExpenseCategory o-- Description
    DailyGroupTransaction o-- Money
    DailyPersonalTransaction o-- Money
    DailyLivingExpense o-- DailyLivingExpenseId
    DailyLivingExpense o-- UserId
    DailyLivingExpense o-- LivingExpenseCategoryId
    DailyLivingExpense o-- Money
    DailyLivingExpense o-- Description
    DailyPersonalExpense o-- UserId
    DailyPersonalExpense o-- SequenceNumber
    DailyPersonalExpense o-- Money
    DailyPersonalExpense o-- Description
    FixedExpenseCategory o-- FixedExpenseCategoryId
    FixedExpenseCategory o-- CategoryName
    FixedExpenseCategory o-- Description
    FixedExpenseCategory o-- Money
    FixedExpenseHistory o-- FixedExpenseHistoryId
    FixedExpenseHistory o-- FixedExpenseCategoryId
    FixedExpenseHistory o-- Year
    FixedExpenseHistory o-- Month
    FixedExpenseHistory o-- Money
    FixedExpenseHistory o-- Description
    MonthlySaving o-- UserId
    MonthlySaving o-- Year
    MonthlySaving o-- Month
    MonthlySaving o-- Money
    MonthlySaving o-- Description

    %% エンティティ間の関係性
    User o-- FinancialAccount
    User o-- MonthlySaving

    UserGroup o-- GroupInvitation
    UserGroup o-- LivingExpenseCategory
    UserGroup o-- MonthlyBudget
    UserGroup o-- FixedExpenseCategory
    UserGroup o-- FixedExpenseHistory

    FinancialAccount o-- BalanceEditHistory

    DailyGroupTransaction o-- DailyLivingExpense
    DailyPersonalTransaction o-- DailyPersonalExpense

    %% サービス層の依存関係
    UserController ..> UserService
    UserGroupController ..> UserGroupService
    TransactionController ..> TransactionService
    BudgetController ..> BudgetService
    ExpenseController ..> ExpenseService
    AccountController ..> AccountService
    SavingController ..> SavingService

    UserService ..> UserRepository
    UserService ..> GroupInvitationRepository
    UserGroupService ..> UserGroupRepository
    UserGroupService ..> GroupInvitationRepository
    TransactionService ..> DailyTransactionRepository
    TransactionService ..> DailyLivingExpenseRepository
    TransactionService ..> DailyPersonalExpenseRepository
    BudgetService ..> MonthlyBudgetRepository
    ExpenseService ..> LivingExpenseCategoryRepository
    ExpenseService ..> FixedExpenseCategoryRepository
    ExpenseService ..> FixedExpenseHistoryRepository
    AccountService ..> FinancialAccountRepository
    AccountService ..> BalanceEditHistoryRepository
    AccountService ..> UserRepository
    SavingService ..> UserRepository
    SavingService ..> MonthlySavingRepository
```

## クラス設計の特徴

### エンティティクラス
- Spring Data JDBCアノテーションを使用したエンティティクラス
- ER図の各テーブルに対応

### リクエストクラス
- REST APIのリクエストボディを表現
- クライアントからサーバーへのデータ送信に使用
- バリデーションアノテーション対応

### サービスクラス
- ビジネスロジックを含む
- トランザクション管理
- 要件定義の各機能に対応

### コントローラークラス
- REST APIエンドポイントを提供
- HTTPリクエストのハンドリング
- レスポンス形式の統一

### リポジトリインターフェース
- データアクセス層
- Spring Data JDBCを使用
- カスタムクエリメソッドを定義
