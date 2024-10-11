// @ts-ignore
/* eslint-disable */

declare namespace API {
  type BaseResponse<T> = {
    code: number;
    data: T;
    message: string;
    description: string;
  };

  type LoginParams = {
    userAccount?: string;
    userPassword?: string;
    autoLogin?: boolean;
    type?: string;
  };

  type RegisterParams = {
    userAccount?: string;
    userPassword?: string;
    checkPassword?: string;
    type?: string;
  };

  type LoginResult = {
    status?: string;
    type?: string;
    currentAuthority?: string;
  };

  type RegisterResult = {
    id: number;
  };

  type CurrentUser = {
    id: number;
    username: string;
    userAccount: string;
    avatarUrl?: string;
    gender: number;
    phone: stirng;
    email: string;
    userStatus: number;
    createTime: Date;
    userRole: number;
  };

  type UserUpdateParams = {
    id: number;
    username: string;
    userAccount: string;
    avatarUrl?: string;
    gender?: number;
    phone?: stirng;
    email?: string;
  };

  type Lesson = {
    content: string;
    courseTime: string;
  }

  type CourseStage = {
    stageName: string;
    lessons: Lesson[];
  }

  type Plan = {
    id: number;
    creatorId: number;
    name: string;
    courseDirection: string;
    subDirection: string;
    courseTarget: string;
    courseDetail: CourseStage[];
    comment: string;
    isPublished: number;
    createTime: Date;
  }

  type PlanCreateParams = {
    name: string;
    courseDirection: string;
    subDirection: string;
    courseTarget?: string;
    courseDetail?: CourseStage[];
    comment?: string;
  }

  type PlanUpdateParams = {
    id: number;
    name: string;
    courseDirection: string;
    subDirection: string;
    courseTarget?: string;
    courseDetail?: CourseStage[];
    comment?: string;
  }

  type RuleListItem = {
    key?: number;
    disabled?: boolean;
    href?: string;
    avatar?: string;
    name?: string;
    owner?: string;
    desc?: string;
    callNo?: number;
    status?: number;
    updatedAt?: string;
    createdAt?: string;
    progress?: number;
  };

  type FakeCaptcha = {
    code?: number;
    status?: string;
  };


  type PageParams = {
    current?: number;
    pageSize?: number;
  };

}
