import { useAuthStyles } from '@/common/styles';
import { Footer } from '@/components';
import { Helmet } from '@umijs/max';
import React from 'react';
import Settings from '../../../../config/defaultSettings';
import UserManageTable from './UserManageTable';

const UserManage: React.FC = () => {
  const { styles } = useAuthStyles();

  return (
    <div id="usermanage" className={styles.container}>
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
        <UserManageTable />
      </div>
      <Footer />
    </div>
  );
};
export default UserManage;
