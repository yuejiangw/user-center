import {Footer} from '@/components';
import {register} from '@/services/ant-design-pro/api';
import {
  AlipayCircleOutlined,
  LockOutlined,
  TaobaoCircleOutlined,
  UserOutlined,
  WeiboCircleOutlined,
} from '@ant-design/icons';
import {LoginForm, ProFormText,} from '@ant-design/pro-components';
import {Helmet, history, useModel} from '@umijs/max';
import {Alert, message, Tabs} from 'antd';
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

const RegisterMessage: React.FC<{
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

const Register: React.FC = () => {
  const [type, setType] = useState<string>('account');
  const {initialState, setInitialState} = useModel('@@initialState');
  const {styles} = useAuthStyles();
  const fetchUserInfo = async () => {
    const userInfo = await initialState?.fetchUserInfo?.();
    if (userInfo) {
      flushSync(() => {
        setInitialState((s) => ({
          ...s,
          currentUser: userInfo,
        }));
      });
    }
  };

  const handleSubmit = async (values: API.RegisterParams) => {
    const {userAccount, userPassword, checkPassword} = values;
    // 校验
    if (userPassword !== checkPassword) {
      message.error('You must input the same password');
      return;
    }

    try {
      // 注册
      const id = await register({
        ...values,
        type,
      });
      const defaultRegisterSuccessMessage = 'Register Successful!';
      message.success(defaultRegisterSuccessMessage);
      await fetchUserInfo();
      history.push('/user/login?redirect');
      // console.log(id);
      return;
      // 如果失败去设置用户错误信息
    } catch (error) {
      const defaultRegisterFailureMessage = 'Register Failed, please try again!';
      console.log(error);
      message.error(defaultRegisterFailureMessage);
    }
  };

  return (
    <div className={styles.container}>
      <Helmet>
        <title>
          {'Register'}- {Settings.title}
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
              submitText: 'Register'
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
                label: 'Register',
              }
            ]}
          />

          {status === 'error' && (
            <RegisterMessage content={'错误的用户名和密码(admin/ant.design)'}/>
          )}
          {type === 'account' && (
            <>
              <ProFormText
                name="userAccount"
                fieldProps={{
                  size: 'large',
                  prefix: <UserOutlined/>,
                }}
                placeholder={'Account'}
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
                placeholder={'Password'}
                rules={[
                  {
                    required: true,
                    message: '密码是必填项！',
                  },
                ]}
              />
              <ProFormText.Password
                name="checkPassword"
                fieldProps={{
                  size: 'large',
                  prefix: <LockOutlined/>,
                }}
                placeholder={'Input password again'}
                rules={[
                  {
                    required: true,
                    message: '校验密码是必填项！',
                  },
                ]}
              />
            </>
          )}

          <div
            style={{
              marginBottom: 24,
            }}
          >

            <p>
              Already have account?
              <a
                style={{float: 'right'}}
                href={'/user/login'}
                rel="noreferrer"
              >
                Login
              </a>
            </p>
          </div>
        </LoginForm>
      </div>
      <Footer/>
    </div>
  );
};
export default Register;
