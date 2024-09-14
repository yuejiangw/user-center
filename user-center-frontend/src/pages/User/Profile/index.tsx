import {Footer} from '@/components';
import {Helmet} from '@umijs/max';
import React from 'react';
import {useAuthStyles} from "@/common/styles";
import UserProfileForm from './UserProfileForm';

const Profile: React.FC = () => {
  const {styles} = useAuthStyles();

  return (
    <div id='usermanage' className={styles.container}>
      <Helmet>
        <title>
          User Profile
        </title>
      </Helmet>
      <div
        style={{
          flex: '1',
          padding: '32px 0',
        }}
      >
      <UserProfileForm />
      </div>
      <Footer/>
    </div>
  );
};
export default Profile;
