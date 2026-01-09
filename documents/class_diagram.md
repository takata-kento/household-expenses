# 家計簿アプリ クラス図

```mermaid
classDiagram
    %% エンティティクラス
    class User {
        -UserId id
        -Username name
        -UserGroupId userGroupId
        -Set~GroupInvitation~ receivedInvitations
        -Integer version
        +leaveGroup() void
        +canCreateGroup() boolean
        +canLeaveGroup() boolean
        +invite(User) GroupInvitationId
        +addInvitation(GroupInvitation) void
        +canInvite(UserId) boolean
        +accept(GroupInvitationId) void
        +reject(GroupInvitationId) void
        +getPendingInvitations() Set~GroupInvitationInfo~
    }

    class UserGroup {
        -UserGroupId id
        -GroupName name
        -Day monthStartDay
        -UserId createdByUserId
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
        -Integer version
        +create(GroupName) UserGroupId
        +updateGroupName(GroupName) void
        +updateMonthStartDay(Day) void
        +canBeModifiedBy(UserId) boolean
    }

    class GroupInvitation {
        -GroupInvitationId id
        -UserGroupId userGroupId
        -UserId invitedUserId
        -UserId invitedByUserId
        -InvitationStatus status
        -LocalDateTime invitedAt
        -LocalDateTime respondedAt
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
        +accept() void
        +reject() void
        +canRespond() boolean
        +isPending() boolean
        +isFrom(UserGroupId) boolean
    }

    class FinancialAccount {
        -FinancialAccountId id
        -AccountName accountName
        -Money balance
        -Boolean isMainAccount
        -List~BalanceEditHistory~ editHistories
        -Integer version
        +addEditHistory(Money, Money, Description) void
        +getLatestEditHistory() BalanceEditHistory
        +updateBalance(Money, Description) void
    }

    class BalanceEditHistory {
        -BalanceEditHistoryId id
        -FinancialAccountId financialAccountId
        -Money oldBalance
        -Money newBalance
        -Description editReason
        -LocalDateTime createdAt
        -Integer version
        +create(FinancialAccountId, Money, Money, Description) BalanceEditHistory
    }

    class MonthlyBudget {
        -MonthlyBudgetId id
        -UserGroupId userGroupId
        -Year year
        -Month month
        -Money budgetAmount
        -UserId setByUserId
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
        -Integer version
        +create(UserGroupId, Year, Month, Money, UserId) MonthlyBudget
        +updateBudgetAmount(Money, UserId) void
        +isCurrentMonth(LocalDate) boolean
        +isSetBy(UserId) boolean
        +calculateRemainingBudget(Money) Money
        +isOverBudget(Money) boolean
    }

    class LivingExpenseCategory {
        -LivingExpenseCategoryId id
        -UserGroupId userGroupId
        -CategoryName categoryName
        -Description description
        -Boolean isDefault
        -Integer version
        +create(CategoryName, Description, UserGroupId) LivingExpenseCategory
        +updateCategoryName(CategoryName) void
        +updateDescription(Description) void
        +markAsDefault() void
        +unmarkAsDefault() void
    }

    class DailyLivingExpense {
        -DailyLivingExpenseId id
        -DailyGroupTransactionId dailyGroupTransactionId
        -UserId userId
        -LivingExpenseCategoryId livingExpenseCategoryId
        -Money amount
        -Description memo
        -Integer version
}

    class DailyPersonalExpense {
        -DailyPersonalExpenseId id
        -DailyPersonalTransactionId dailyPersonalTransactionId
        -Money amount
        -Description memo
        -Integer version
    }

    class DailyGroupTransaction {
        -DailyGroupTransactionId id
        -UserGroupId userGroupId
        -LocalDate transactionDate
        -List~DailyLivingExpense~ livingExpenses
        -Integer version
        +addLivingExpense(UserId, LivingExpenseCategoryId, Money, Description) void
        +calculateTotalLivingExpense() Money
}

    class DailyPersonalTransaction {
        -DailyPersonalTransactionId id
        -UserId userId
        -LocalDate transactionDate
        -Money income
        -List~DailyPersonalExpense~ personalExpenses
        -Integer version
        +addPersonalExpense(Money, Description) void
        +calculateTotalExpense(Money) void
        +getTotalPersonalExpense() Money
}

    class FixedExpenseCategory {
        -FixedExpenseCategoryId id
        -UserGroupId userGroupId
        -CategoryName categoryName
        -Description description
        -Money defaultAmount
        -Integer version
        +updateCategoryName(CategoryName) void
        +updateDescription(Description) void
        +updateDefaultAmount(Money) void
        +validateCategoryName(CategoryName) boolean
        +validateDefaultAmount(Money) boolean
        +isValidForUserGroup(UserGroupId) boolean
        +canBeDeleted() boolean
    }

    class FixedExpenseHistory {
        -FixedExpenseHistoryId id
        -FixedExpenseCategoryId categoryId
        -Year year
        -Month month
        -Money amount
        -LocalDate effectiveDate
        -Description memo
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
        -Integer version
        +updateAmount(Money, Description) void
        +updateEffectiveDate(LocalDate) void
        +updateMemo(Description) void
        +validateAmount(Money) boolean
        +isForCategory(FixedExpenseCategoryId) boolean
        +isForPeriod(Year, Month) boolean
        +canBeModified() boolean
    }

    class MonthlySaving {
        -UserId userId
        -Year year
        -Month month
        -Money savingAmount
        -FinancialAccountId financialAccountId
        -Description memo
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
        -Integer version
        +updateSavingAmount(Money) void
        +updateFinancialAccount(FinancialAccountId) void
        +updateMemo(Description) void
        +validateSavingAmount(Money) boolean
        +isForUser(UserId) boolean
        +isForPeriod(Year, Month) boolean
        +canBeDeleted() boolean
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
        -LivingExpenseCategoryId categoryId
        -Money amount
        -Description memo
        +getCategoryId() LivingExpenseCategoryId
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

    class GroupInvitationInfo {
        -GroupInvitationId groupInvitationId
        -UserGroupId userGroupId
        -UserId invitedByUserId
        +getGroupInvitationId() GroupInvitationId
        +getUserGroupId() UserGroupId
        +getInvitedByUserId() UserId
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
        -UUID value
        +value() UUID
        +toString() String
    }

    class UserGroupId {
        <<record>>
        -UUID value
        +value() UUID
        +toString() String
    }

    class GroupInvitationId {
        <<record>>
        -UUID value
        +value() UUID
        +toString() String
    }

    class FinancialAccountId {
        <<record>>
        -UUID value
        +value() UUID
        +toString() String
    }

    class Day {
        <<record>>
        -int value
        +value() int
    }

    class GroupName {
        <<record>>
        -String value
        +value() String
    }

    class SequenceNumber {
        <<record>>
        -int value
        +value() int
    }

    class LivingExpenseCategoryId {
        <<record>>
        -UUID value
        +value() UUID
        +toString() String
    }

    class FixedExpenseCategoryId {
        <<record>>
        -UUID value
        +value() UUID
        +toString() String
    }

    class BalanceEditHistoryId {
        <<record>>
        -UUID value
        +value() UUID
        +toString() String
    }

    class DailyLivingExpenseId {
        <<record>>
        -UUID value
        +value() UUID
        +toString() String
    }

    class FixedExpenseHistoryId {
        <<record>>
        -UUID value
        +value() UUID
        +toString() String
    }

    class MonthlyBudgetId {
        <<record>>
        -UUID value
        +value() UUID
        +toString() String
    }

    class DailyGroupTransactionId {
        <<record>>
        -UUID value
        +value() UUID
        +toString() String
    }

    class DailyPersonalTransactionId {
        <<record>>
        -UUID value
        +value() UUID
        +toString() String
    }

    class DailyPersonalExpenseId {
        <<record>>
        -UUID value
        +value() UUID
        +toString() String
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
        +getDailyTransaction(UserId, LocalDate) DailyTransaction
        +updateDailyTransaction(UserId, LocalDate, DailyTransactionRequest) DailyTransaction
        +deleteDailyTransaction(UserId, LocalDate) void
    }

    class BudgetService {
        -MonthlyBudgetRepository monthlyBudgetRepository
        +setMonthlyBudget(MonthlyBudgetRequest) MonthlyBudget
        +getMonthlyBudget(UserGroupId, Integer, Integer) MonthlyBudget
        +calculateBudgetBalance(UserGroupId, LocalDate, BigDecimal) BigDecimal
    }

    class ExpenseService {
        -LivingExpenseCategoryRepository livingExpenseCategoryRepository
        -FixedExpenseCategoryRepository fixedExpenseCategoryRepository
        -FixedExpenseHistoryRepository fixedExpenseHistoryRepository
        +createLivingExpenseCategory(UserGroupId, String, String) LivingExpenseCategory
        +updateLivingExpenseCategory(LivingExpenseCategoryId, String, String) LivingExpenseCategory
        +deleteLivingExpenseCategory(LivingExpenseCategoryId) void
        +createFixedExpenseCategory(UserGroupId, String, String, BigDecimal) FixedExpenseCategory
        +setFixedExpenseAmount(FixedExpenseCategoryId, Integer, Integer, BigDecimal) FixedExpenseHistory
        +getFixedExpenses(UserGroupId, Integer, Integer) List~FixedExpenseHistory~
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
        +updateLivingExpenseCategory(LivingExpenseCategoryId, String, String) ResponseEntity~LivingExpenseCategory~
        +deleteLivingExpenseCategory(LivingExpenseCategoryId) ResponseEntity~Void~
        +createFixedExpenseCategory(String, String, BigDecimal) ResponseEntity~FixedExpenseCategory~
        +setFixedExpenseAmount(FixedExpenseCategoryId, Integer, Integer, BigDecimal) ResponseEntity~FixedExpenseHistory~
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
        <<extends CrudRepository~User, UserId~>>
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
        +findByFinancialAccountIdOrderByIdDesc(FinancialAccountId) List~BalanceEditHistory~
    }

    class MonthlyBudgetRepository {
        <<interface>>
        +findByUserGroupIdAndYearAndMonth(UserGroupId, Integer, Integer) Optional~MonthlyBudget~
    }

    class LivingExpenseCategoryRepository {
        <<interface>>
        +findByUserGroupId(UserGroupId) List~LivingExpenseCategory~
        +findByUserGroupIdAndIsDefault(UserGroupId, Boolean) List~LivingExpenseCategory~
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
        +findByUserGroupId(UserGroupId) List~FixedExpenseCategory~
    }

    class FixedExpenseHistoryRepository {
        <<interface>>
        +findByFixedExpenseCategoryIdAndYearAndMonth(FixedExpenseCategoryId, Integer, Integer) Optional~FixedExpenseHistory~
        +findByFixedExpenseCategoryIdInAndYear(List~FixedExpenseCategoryId~, Integer) List~FixedExpenseHistory~
    }

    class MonthlySavingRepository {
        <<interface>>
        +findByUserIdAndYearAndMonth(UserId, Year, Month) Optional~MonthlySaving~
        +findByUserIdAndYear(UserId, Year) List~MonthlySaving~
    }

    %% エンティティ間の関係性
    User o-- GroupInvitation

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
