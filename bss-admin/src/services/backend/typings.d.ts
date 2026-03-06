declare namespace API {
  type Admin = {
    id: string;
    locale: 'zh_CN' | 'en_US';
    username: string;
    password: string;
    name: string;
    status: 'NORMAL' | 'DISABLE' | 'DELETED';
    lock: number;
    title?: string;
    email?: string;
    avatar?: string;
    role?: string;
    authKey?: string;
    createdDate: string;
    updateDate: string;
    version: number;
  };

  type AdminForm = {
    username: string;
    password?: string;
    status: 'NORMAL' | 'DISABLE' | 'DELETED';
    role?: string;
    isRobotAdmin: boolean;
    name?: string;
    title?: string;
    email?: string;
    avatar?: string;
  };

  type AdminModel = {
    id: string;
    locale: 'zh_CN' | 'en_US';
    name: string;
    avatar?: string;
    username: string;
    title?: string;
    bindAuthKey: boolean;
    role?: string;
    modules: string[];
  };

  type AllSetting = {
    id: string;
    setting: Record<string, any>;
    settingType: string;
    createdDate: string;
    updateDate: string;
    version: number;
  };

  type AuthSetting = {
    authLoginErrorMax: number;
    authLoginTokenMax: number;
    timeOutHours: number;
    timeOutDelayMinutes: number;
    sessionTimeOutHours: number;
    loginNeedAuthCodeCount: number;
    sessionMax: number;
  };

  type BindGoogleAuthForm = {
    authKey: string;
    authCode: number;
  };

  type ChangePwdForm = {
    password: string;
    oldPassword: string;
  };

  type deleteSysAdminDeleteAllParams = {
    id: string[];
  };

  type deleteSysAdminParams = {
    id: string;
  };

  type deleteSysModuleDeleteAllParams = {
    id: string[];
  };

  type deleteSysModuleParams = {
    id: string;
  };

  type deleteSysRoleDeleteAllParams = {
    id: string[];
  };

  type deleteSysRoleParams = {
    id: string;
  };

  type deleteTestDbDeleteAllParams = {
    id: string[];
  };

  type deleteTestDbParams = {
    id: string;
  };

  type getSysAdminAllParams = {
    ids: string[];
  };

  type getSysAdminParams = {
    id: string;
  };

  type getSysModuleAllParams = {
    ids: string[];
  };

  type getSysModuleParams = {
    id: string;
  };

  type getSysRoleAllParams = {
    ids: string[];
  };

  type getSysRoleParams = {
    id: string;
  };

  type getSysSettingParams = {
    id: string;
  };

  type getTestDbAllParams = {
    ids: string[];
  };

  type getTestDbParams = {
    id: string;
  };

  type LoginForm = {
    account: string;
    password: string;
    authCode?: string;
  };

  type LoginLog = {
    id: string;
    userId: string;
    username: string;
    type: string;
    sessionId?: string;
    role?: string;
    avatar?: string;
    locale: 'zh_CN' | 'en_US';
    password?: string;
    clientUa?: string;
    client?: string;
    platform: 'android' | 'ios' | 'web';
    token: string;
    logoutClientUa?: string;
    logoutClient?: string;
    logoutDate?: string;
    isMyUser: boolean;
    isAgent: boolean;
    createdDate: string;
    updateDate: string;
    version: number;
    myUser?: boolean;
    agent?: boolean;
  };

  type PageAdmin = {
    pageSize: number;
    current: number;
    total: number;
    data: Admin[];
  };

  type PageQuery = {
    current: number;
    pageSize: number;
    sort?: Record<string, any>;
    filter: Record<string, any>;
  };

  type PageSysModuleModel = {
    pageSize: number;
    current: number;
    total: number;
    data: SysModuleModel[];
  };

  type PageSysRole = {
    pageSize: number;
    current: number;
    total: number;
    data: SysRole[];
  };

  type PageTestDb = {
    pageSize: number;
    current: number;
    total: number;
    data: TestDb[];
  };

  type postSysAdminParams = {
    id: string;
  };

  type postSysAdminUnbindAuthKeyParams = {
    id: string;
  };

  type postSysAdminUnlockParams = {
    id: string;
  };

  type postSysModuleParams = {
    id: string;
  };

  type postSysRoleParams = {
    id: string;
  };

  type postTestDbParams = {
    id: string;
  };

  type SysModuleForm = {
    id: string;
    path?: string;
    name: string;
    parent?: string;
  };

  type SysModuleModel = {
    id: string;
    parent?: string;
    path?: string;
    name?: string;
    children?: SysModuleModel[];
    createDate?: string;
    updateDate?: string;
  };

  type SysRole = {
    id: string;
    name: string;
    moduleId: string[];
    createDate?: string;
    updateDate?: string;
  };

  type SysRoleForm = {
    id: string;
    name: string;
    moduleId: string[];
  };

  type TestDb = {
    id: string;
    name: string;
    age: number;
    amount: number;
    createDay: string;
    status: 'NORMAL' | 'DISABLE' | 'DELETED';
    imgUrl: string;
    markdownContent: string;
    fullContent: string;
    createdDate: string;
    updateDate: string;
    version: number;
  };

  type TestDbForm = {
    name: string;
    age: number;
    amount: number;
    status: 'NORMAL' | 'DISABLE' | 'DELETED';
    imgUrl?: string;
    markdownContent?: string;
    fullContent?: string;
  };

  type Unit = true;

  type UpdateAdminForm = {
    name?: string;
    title?: string;
    email?: string;
    avatar?: string;
  };
}
