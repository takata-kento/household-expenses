# 家計簿アプリ クラス図

```mermaid
classDiagram
    %% エンティティクラス
    class User {
        -UserId id
        -Username name
        -Optional~UserGroupId~ userGroupId
        -Set~GroupInvitation~ receivedInvitations
        -Integer version
        +isBelongsToGroup() boolean
        +receivedInvitations() Set~GroupInvitationInfo~
        +getPendingInvitations() Set~GroupInvitationInfo~
        +leaveGroup() void
        +canCreateGroup() boolean
        +canLeaveGroup() boolean
        +canInvite(UserId) boolean
        +invite(User) GroupInvitationId
        +accept(GroupInvitationId) void
        +reject(GroupInvitationId) void
    }

    class UserGroup {
        -UserGroupId id
        -GroupName name
        -Day monthStartDay
        -UserId createdByUserId
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
        -Integer version
        +create(GroupName, Day, UserId) UserGroup
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
        +create(UserGroupId, UserId, UserId) GroupInvitation
        +accept() void
        +reject() void
        +isPending() boolean
        +canRespond() boolean
        +isFrom(UserGroupId) boolean
    }

    class FinancialAccount {
        -FinancialAccountId id
        -UserId userId
        -BankName bankName
        -Optional~AccountName~ accountName
        -Money balance
        -Boolean isMainAccount
        -Set~BalanceEditHistory~ editHistories
        -Integer version
        +editHistories() Set~BalanceEditHistoryInfo~
        +latestEditHistory() Optional~BalanceEditHistoryInfo~
        +updateBalance(Money, LocalDate) void
        +updateBalance(Money, Description, LocalDate) void
        +updateAccountName(AccountName) void
    }

    class BalanceEditHistory {
        -BalanceEditHistoryId id
        -FinancialAccountId financialAccountId
        -Money oldBalance
        -Money newBalance
        -Optional~Description~ editReason
        -LocalDate editedAt
        -LocalDateTime createdAt
        -Integer version
        ~create(FinancialAccountId, Money, Money, Optional~Description~, LocalDate) BalanceEditHistory
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
        +belongsTo(UserGroupId) boolean
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
        -Set~DailyLivingExpense~ livingExpenses
        -Integer version
        +livingExpenses() List~DailyLivingExpenseInfo~
        +addLivingExpense(UserId, LivingExpenseCategoryId, Money, Description) void
        +removeLivingExpensesOf(UserId) void
        +calculateTotalLivingExpense() Money
}

    class DailyPersonalTransaction {
        -DailyPersonalTransactionId id
        -UserId userId
        -LocalDate transactionDate
        -Money income
        -List~DailyPersonalExpense~ personalExpenses
        -Integer version
        +personalExpenses() List~DailyPersonalExpenseInfo~
        +addPersonalExpense(Money, Description) void
        +clearPersonalExpenses() void
        +updateIncome(Money) void
        +calculateTotalPersonalExpense() Money
}

    class FixedExpenseCategory {
        -FixedExpenseCategoryId id
        -UserGroupId userGroupId
        -CategoryName categoryName
        -Description description
        -Money defaultAmount
        -Integer version
        +create(CategoryName, Description, Money, UserGroupId) FixedExpenseCategory
        +updateCategoryName(CategoryName) void
        +updateDescription(Description) void
        +updateDefaultAmount(Money) void
        +belongsTo(UserGroupId) boolean
    }

    class FixedExpenseHistory {
        -FixedExpenseHistoryId id
        -FixedExpenseCategoryId fixedExpenseCategoryId
        -Year year
        -Month month
        -Money amount
        -LocalDate effectiveDate
        -Optional~Description~ memo
        -Integer version
        +create(FixedExpenseCategoryId, Year, Month, Money, LocalDate, Optional~Description~) FixedExpenseHistory
        +updateAmount(Money) void
        +updateEffectiveDate(LocalDate) void
        +updateMemo(Optional~Description~) void
    }

    class MonthlySaving {
        -MonthlySavingId id
        -UserId userId
        -Year year
        -Month month
        -Money savingAmount
        -FinancialAccountId financialAccountId
        -Optional~Description~ memo
        -Integer version
        +create(UserId, Year, Month, Money, FinancialAccountId, Optional~Description~) MonthlySaving
        +updateSavingAmount(Money) void
        +updateFinancialAccount(FinancialAccountId) void
        +updateMemo(Optional~Description~) void
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
    %% 注: 上記 *Request 群は Presentation層（TransactionController）と共に未実装。

    %% TransactionService の入出力型（Application層・実装済み）
    class LivingExpenseInput {
        <<record>>
        -LivingExpenseCategoryId categoryId
        -Money amount
        -Description memo
    }

    class PersonalExpenseInput {
        <<record>>
        -Money amount
        -Description memo
    }

    class DailyLivingExpenseInfo {
        <<record>>
        -DailyLivingExpenseId id
        -UserId userId
        -LivingExpenseCategoryId livingExpenseCategoryId
        -Money amount
        -Description memo
        +from(DailyLivingExpense) DailyLivingExpenseInfo
    }

    class DailyPersonalExpenseInfo {
        <<record>>
        -DailyPersonalExpenseId id
        -Money amount
        -Description memo
        +from(DailyPersonalExpense) DailyPersonalExpenseInfo
    }

    class DailyTransactionInfo {
        <<record>>
        -LocalDate transactionDate
        -Money income
        -List~DailyLivingExpenseInfo~ livingExpenses
        -List~DailyPersonalExpenseInfo~ personalExpenses
        -Money totalLivingExpense
        -Money totalPersonalExpense
        -Money totalExpense
        -Money budgetBalance
    }

    DailyTransactionInfo o-- DailyLivingExpenseInfo
    DailyTransactionInfo o-- DailyPersonalExpenseInfo

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

    class BankName {
        <<record>>
        -String value
        +value() String
    }

    class UserId {
        <<record>>
        -UUID value
        +value() UUID
    }

    class UserGroupId {
        <<record>>
        -UUID value
        +value() UUID
    }

    class GroupInvitationId {
        <<record>>
        -UUID value
        +value() UUID
    }

    class FinancialAccountId {
        <<record>>
        -String value
        +value() String
    }
    %% FinancialAccountId は銀行の口座番号（6〜8桁の数字文字列）。生成時に検証する。

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
    }

    class FixedExpenseCategoryId {
        <<record>>
        -UUID value
        +value() UUID
    }

    class BalanceEditHistoryId {
        -Long value
        +getValue() Long
        +isValid() boolean
    }

    class DailyLivingExpenseId {
        <<record>>
        -String value
        +value() String
        +generate() DailyLivingExpenseId
    }

    class FixedExpenseHistoryId {
        <<record>>
        -long value
        +value() long
    }

    class MonthlyBudgetId {
        <<record>>
        -UUID value
        +value() UUID
    }

    class MonthlySavingId {
        <<record>>
        -UUID value
        +value() UUID
    }

    class DailyGroupTransactionId {
        <<record>>
        -String value
        +value() String
        +generate() DailyGroupTransactionId
    }

    class DailyPersonalTransactionId {
        <<record>>
        -String value
        +value() String
        +generate() DailyPersonalTransactionId
    }

    class DailyPersonalExpenseId {
        <<record>>
        -String value
        +value() String
        +generate() DailyPersonalExpenseId
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
        -CognitoUserContext cognitoUserContext
        -getCurrentUser() User
        +acceptGroupInvitation(GroupInvitationId) void
        +rejectGroupInvitation(GroupInvitationId) void
    }

    class UserGroupService {
        -UserGroupRepository userGroupRepository
        -UserRepository userRepository
        -CognitoUserContext cognitoUserContext
        +createGroup(GroupName) UserGroup
        +inviteUser(Username) GroupInvitationId
        +leaveGroup() void
        +getGroupMembers() List~User~
        +updateGroupName(GroupName) UserGroup
        +updateMonthStartDay(Day) UserGroup
    }

    class TransactionService {
        -DailyGroupTransactionRepository dailyGroupTransactionRepository
        -DailyPersonalTransactionRepository dailyPersonalTransactionRepository
        -UserRepository userRepository
        -BudgetService budgetService
        -getCurrentUser(UserId) User
        -currentUserGroupId(User) UserGroupId
        -groupMemberCount(UserGroupId) int
        -buildInfo(UserId, LocalDate, DailyGroupTransaction, DailyPersonalTransaction, int) DailyTransactionInfo
        +recordDailyTransaction(UserId, LocalDate, Money, List~LivingExpenseInput~, List~PersonalExpenseInput~) DailyTransactionInfo
        +calculateTotalExpense(Money, Money, int) Money
        +getDailyTransaction(UserId, LocalDate) DailyTransactionInfo
        +updateDailyTransaction(UserId, LocalDate, Money, List~LivingExpenseInput~, List~PersonalExpenseInput~) DailyTransactionInfo
        +deleteDailyTransaction(UserId, LocalDate) void
    }
    %% 注: TransactionService は2集約 (DailyGroupTransaction=生活費・共有 / DailyPersonalTransaction=収入+個人支出・非共有) をまたいで記録する。
    %% 支出合計 = 切り上げ(生活費合計 ÷ グループ人数) + 個人支出合計。予算残金は BudgetService.calculateBudgetBalance で算出。
    %% 戻り値は集約 record DailyTransactionInfo。入力は LivingExpenseInput / PersonalExpenseInput。
    %% Controller (TransactionController) と DailyTransactionRequest 等のリクエストDTOは未実装（Presentation層と共に保留）。

    class BudgetService {
        -UserRepository userRepository
        -UserGroupRepository userGroupRepository
        -MonthlyBudgetRepository monthlyBudgetRepository
        -DailyGroupTransactionRepository dailyGroupTransactionRepository
        +setMonthlyBudget(Year, Month, Money) MonthlyBudget
        +getMonthlyBudget(Year, Month) MonthlyBudget
        +getMonthlyBudgetsByYear(Year) List~MonthlyBudget~
        +calculateBudgetBalance(LocalDate) Money
    }

    class ExpenseService {
        -UserRepository userRepository
        -LivingExpenseCategoryRepository livingExpenseCategoryRepository
        -FixedExpenseCategoryRepository fixedExpenseCategoryRepository
        -FixedExpenseHistoryRepository fixedExpenseHistoryRepository
        -getCurrentUser() User
        -getCurrentUserGroupId() UserGroupId
        +createLivingExpenseCategory(CategoryName, Description) LivingExpenseCategory
        +updateLivingExpenseCategory(LivingExpenseCategoryId, CategoryName, Description) LivingExpenseCategory
        +deleteLivingExpenseCategory(LivingExpenseCategoryId) void
        +createFixedExpenseCategory(CategoryName, Description, Money) FixedExpenseCategory
        +setFixedExpenseAmount(FixedExpenseCategoryId, Year, Month, Money, LocalDate, Optional~Description~) FixedExpenseHistory
        +getFixedExpenses(Year, Month) List~FixedExpenseHistory~
    }

    class AccountService {
        -FinancialAccountRepository financialAccountRepository
        +createAccount(FinancialAccountId, BankName, Optional~AccountName~, Money, Boolean) FinancialAccount
        +updateAccountName(FinancialAccountId, AccountName) FinancialAccount
        +updateBalance(FinancialAccountId, Money) FinancialAccount
        +updateBalance(FinancialAccountId, Money, Description) FinancialAccount
        +calculateNewBalance(FinancialAccountId, Money, Money, Money, Money) Money
        +getUserAccounts() List~FinancialAccount~
    }

    class SavingService {
        -MonthlySavingRepository monthlySavingRepository
        -FinancialAccountRepository financialAccountRepository
        -UserRepository userRepository
        -CognitoUserContext cognitoUserContext
        -getCurrentUser() User
        -verifyAccountOwnedBy(FinancialAccountId, UserId) void
        -loadOwnedSaving(Year, Month, UserId) MonthlySaving
        +recordMonthlySaving(Year, Month, Money, FinancialAccountId, Optional~Description~) MonthlySaving
        +getMonthlySaving(Year, Month) MonthlySaving
        +updateMonthlySaving(Year, Month, Money, FinancialAccountId, Optional~Description~) MonthlySaving
        +deleteMonthlySaving(Year, Month) void
        +getSavingsByYear(Year) List~MonthlySaving~
    }

    %% コントローラークラス
    class UserController {
        -UserService userService
        +acceptGroupInvitation(GroupInvitationId) ResponseEntity~Void~
        +rejectGroupInvitation(GroupInvitationId) ResponseEntity~Void~
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
        +createAccount(FinancialAccountId, BankName, Optional~AccountName~, Money, Boolean) ResponseEntity~FinancialAccount~
        +updateAccountName(FinancialAccountId, AccountName) ResponseEntity~FinancialAccount~
        +updateBalance(FinancialAccountId, Money) ResponseEntity~FinancialAccount~
        +updateBalance(FinancialAccountId, Money, Description) ResponseEntity~FinancialAccount~
        +getUserAccounts() ResponseEntity~List~FinancialAccount~~
    }

    class SavingController {
        -SavingService savingService
        +recordMonthlySaving(Year, Month, Money, FinancialAccountId, Optional~Description~) ResponseEntity~MonthlySaving~
        +getMonthlySaving(Year, Month) ResponseEntity~MonthlySaving~
        +updateMonthlySaving(Year, Month, Money, FinancialAccountId, Optional~Description~) ResponseEntity~MonthlySaving~
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

    class FinancialAccountRepository {
        <<interface>>
        <<extends CrudRepository~FinancialAccount, FinancialAccountId~>>
        +findByUserId(UserId) List~FinancialAccount~
    }

    class MonthlyBudgetRepository {
        <<interface>>
        <<extends CrudRepository~MonthlyBudget, MonthlyBudgetId~>>
        +findByUserGroupIdAndYearAndMonth(UserGroupId, Year, Month) Optional~MonthlyBudget~
        +findByUserGroupId(UserGroupId) List~MonthlyBudget~
        +existsByUserGroupIdAndYearAndMonth(UserGroupId, Year, Month) boolean
        +findByUserGroupIdAndYear(UserGroupId, Year) List~MonthlyBudget~
    }

    class LivingExpenseCategoryRepository {
        <<interface>>
        +findByUserGroupId(UserGroupId) List~LivingExpenseCategory~
        +findByUserGroupIdAndIsDefault(UserGroupId, Boolean) List~LivingExpenseCategory~
    }

    class FixedExpenseCategoryRepository {
        <<interface>>
        +findByUserGroupId(UserGroupId) List~FixedExpenseCategory~
    }

    class FixedExpenseHistoryRepository {
        <<interface>>
        <<extends CrudRepository~FixedExpenseHistory, FixedExpenseHistoryId~>>
        +findByFixedExpenseCategoryIdAndYearAndMonth(FixedExpenseCategoryId, Year, Month) Optional~FixedExpenseHistory~
        +findByFixedExpenseCategoryIdInAndYearAndMonth(Collection~FixedExpenseCategoryId~, Year, Month) List~FixedExpenseHistory~
    }

    class MonthlySavingRepository {
        <<interface>>
        <<extends CrudRepository~MonthlySaving, MonthlySavingId~>>
        +findByUserIdAndYearAndMonth(UserId, Year, Month) Optional~MonthlySaving~
        +findByUserIdAndYear(UserId, Year) List~MonthlySaving~
    }

    class DailyGroupTransactionRepository {
        <<interface>>
        <<extends CrudRepository~DailyGroupTransaction, DailyGroupTransactionId~>>
        +findByUserGroupIdAndTransactionDate(UserGroupId, LocalDate) Optional~DailyGroupTransaction~
        +findByUserGroupId(UserGroupId) List~DailyGroupTransaction~
        +findByUserGroupIdAndTransactionDateBetween(UserGroupId, LocalDate, LocalDate) List~DailyGroupTransaction~
    }

    class DailyPersonalTransactionRepository {
        <<interface>>
        <<extends CrudRepository~DailyPersonalTransaction, DailyPersonalTransactionId~>>
        +findByUserIdAndTransactionDate(UserId, LocalDate) Optional~DailyPersonalTransaction~
        +findByUserId(UserId) List~DailyPersonalTransaction~
        +existsByUserIdAndTransactionDate(UserId, LocalDate) boolean
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
    UserService ..> CognitoUserContext
    UserGroupService ..> UserGroupRepository
    BudgetService ..> UserRepository
    BudgetService ..> UserGroupRepository
    BudgetService ..> MonthlyBudgetRepository
    BudgetService ..> DailyGroupTransactionRepository
    TransactionService ..> DailyGroupTransactionRepository
    TransactionService ..> DailyPersonalTransactionRepository
    TransactionService ..> UserRepository
    TransactionService ..> BudgetService
    ExpenseService ..> UserRepository
    ExpenseService ..> LivingExpenseCategoryRepository
    ExpenseService ..> FixedExpenseCategoryRepository
    ExpenseService ..> FixedExpenseHistoryRepository
    AccountService ..> FinancialAccountRepository
    AccountService ..> UserRepository
    SavingService ..> UserRepository
    SavingService ..> MonthlySavingRepository
    SavingService ..> FinancialAccountRepository
    SavingService ..> UserRepository
    SavingService ..> CognitoUserContext
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

## サービスクラスの業務ルール

### AccountService
- **現在ユーザーの取得**: `CognitoUserContext.currentUserId()`から取得（メソッド引数では受け取らない）
- **口座番号の重複検査**: `createAccount`では事前に`findById`で同一口座番号の既存口座を確認し、存在する場合は`IllegalStateException`をスローする。所有者で区別したメッセージを返す。
  - 既存口座が現在ユーザー所有 → `"Account is already registered: {id}"`（メイン口座か否かを問わず重複登録不可）
  - 既存口座が他ユーザー所有 → `"Account number is already used by another user: {id}"`
- **メイン口座の一意性**: `createAccount`で`isMainAccount=true`を指定する場合、同一ユーザーに既に`isMainAccount=true`の口座があれば`IllegalStateException`をスローする（要件定義「金融口座のうち一つの口座を変動費の記録機能群で使用される残高として設定する」に基づく）
- **口座名の任意設定**: `accountName`は作成時に`Optional<AccountName>`として受け取り省略可。後から`updateAccountName(FinancialAccountId, AccountName)`で設定・更新できる（所有者チェック付き）。
- **所有権チェック**: `updateAccountName` / `updateBalance` / `calculateNewBalance`は対象口座の`userId`が現在ユーザーと一致しない場合`IllegalStateException`をスローする（要件定義「非共有データ：預金残高は記録者本人のみ閲覧可能」に基づく）
- **残高更新と編集履歴**: `updateBalance(FinancialAccountId, Money)`（自動計算用）と`updateBalance(FinancialAccountId, Money, Description)`（手動編集用）の2つのオーバーロードを提供。両方とも編集履歴を記録するが、前者は編集理由なし（`Optional.empty`）、後者は編集理由ありで記録する
