import {Footer} from '@/components';
import {
  AlipayCircleOutlined,
  TaobaoCircleOutlined,
  WeiboCircleOutlined,
} from '@ant-design/icons';
import {Helmet, useModel} from '@umijs/max';
import {Alert} from 'antd';
import React from 'react';
import Settings from '../../../../config/defaultSettings';
import {useAuthStyles} from "@/common/styles";
import Table from './table'


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

const UserManage: React.FC = () => {
  // const {initialState, setInitialState} = useModel('@@initialState');
  const {styles} = useAuthStyles();

  return (
    <div id='usermanage' className={styles.container}>
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
      <Table />
      </div>
      <Footer/>
    </div>
  );
};
export default UserManage;
