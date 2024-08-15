import {Footer} from '@/components';
import {login} from '@/services/ant-design-pro/api';
import {
  AlipayCircleOutlined,
  LockOutlined,
  TaobaoCircleOutlined,
  UserOutlined,
  WeiboCircleOutlined,
} from '@ant-design/icons';
import {
  LoginForm,
  ProFormCheckbox,
  ProFormText,
} from '@ant-design/pro-components';
import {Helmet, history, useModel} from '@umijs/max';
import {Alert, Tabs, message} from 'antd';
import React, {useState} from 'react';
import {flushSync} from 'react-dom';
import Settings from '../../../../config/defaultSettings';
import {useAuthStyles} from "@/common/styles";

const ActionIcons = () => {
  const {styles} = useAuthStyles();
  return (
    <>
      <AlipayCircleOutlined key="AlipayCircleOutlined" className={styles.action}/>
      <TaobaoCircleOutlined key="TaobaoCircleOutlined" className={styles.action}/>
      <WeiboCircleOutlined key="WeiboCircleOutlined" className={styles.action}/>
    </>
  );
};

const LoginMessage: React.FC<{
  content: string;
}> = ({content}) => {
  return (
    <Alert
      style={{
        marginBottom: 24,
      }}
      message={content}
      type="error"
      showIcon
    />
  );
};

const Login: React.FC = () => {
  const [userLoginState, setUserLoginState] = useState<API.LoginResult>({});
  const [type, setType] = useState<string>('account');
  const {initialState, setInitialState} = useModel('@@initialState');
  const {styles} = useAuthStyles();
  const fetchUserInfo = async () => {
    const userInfo = await initialState?.fetchUserInfo?.();
    console.log("userInfo: " + userInfo);
    if (userInfo) {
      flushSync(() => {
        setInitialState((s) => ({
          ...s,
          currentUser: userInfo,
        }));
      });
    }
  };

  const handleSubmit = async (values: API.LoginParams) => {
    try {
      // 登录
      const user = await login({
        ...values,
        type,
      });
      if (user) {
        const defaultLoginSuccessMessage = 'Login Successful!';
        message.success(defaultLoginSuccessMessage);
        const user = await fetchUserInfo();
        console.log("handleSubmit: " + user);
        const urlParams = new URL(window.location.href).searchParams;
        history.push(urlParams.get('redirect') || '/');
        return;
      }
      console.log(user);
      // 如果失败去设置用户错误信息
      setUserLoginState(user);
    } catch (error) {
      const defaultLoginFailureMessage = 'Login Failed, please try again!';
      console.log(error);
      message.error(defaultLoginFailureMessage);
    }
  };

  const {status, type: loginType} = userLoginState;
  return (
    <div className={styles.container}>
      <Helmet>
        <title>
          {'Login'}- {Settings.title}
        </title>
      </Helmet>
      <div
        style={{
          flex: '1',
          padding: '32px 0',
        }}
      >
        <LoginForm
          submitter={{
            searchConfig: {
              submitText: 'Login'
            }
          }}
          contentStyle={{
            minWidth: 280,
            maxWidth: '75vw',
          }}
          logo={<img alt="logo" src="/logo.svg"/>}
          title="User Center"
          subTitle={'A fullstack project based on SpringBoot and React'}
          initialValues={{
            autoLogin: true,
          }}
          actions={['Or login with :', <ActionIcons key="icons"/>]}
          onFinish={async (values) => {
            await handleSubmit(values as API.LoginParams);
          }}
        >
          <Tabs
            activeKey={type}
            onChange={setType}
            centered
            items={[
              {
                key: 'account',
                label: 'Login',
              }
            ]}
          />

          {status === 'error' && loginType === 'account' && (
            <LoginMessage content={'错误的用户名和密码(admin/ant.design)'}/>
          )}
          {type === 'account' && (
            <>
              <ProFormText
                name="userAccount"
                fieldProps={{
                  size: 'large',
                  prefix: <UserOutlined/>,
                }}
                placeholder={'用户名: admin or user'}
                rules={[
                  {
                    required: true,
                    message: '用户名是必填项！',
                  },
                ]}
              />
              <ProFormText.Password
                name="userPassword"
                fieldProps={{
                  size: 'large',
                  prefix: <LockOutlined/>,
                }}
                placeholder={'密码: ant.design'}
                rules={[
                  {
                    required: true,
                    message: '密码是必填项！',
                  },
                ]}
              />
            </>
          )}

          {status === 'error' && loginType === 'mobile' && <LoginMessage content="验证码错误"/>}

          <div
            style={{
              marginBottom: 24,
            }}
          >
            <ProFormCheckbox noStyle name="autoLogin">
              自动登录
            </ProFormCheckbox>
            <a
              style={{float: 'right'}}
              href={'/user/register'}
              // target="_blank"
              rel="noreferrer"
            >
              Register
            </a>
          </div>
        </LoginForm>
      </div>
      <Footer/>
    </div>
  );
};
export default Login;
