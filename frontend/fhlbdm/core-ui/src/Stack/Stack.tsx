import React from 'react';
import { twMerge } from 'tailwind-merge';

type Direction = 'row' | 'col' | 'row-reverse' | 'col-reverse';

interface StackProps extends React.HTMLAttributes<HTMLDivElement> {
  direction?: Direction;
  className?: string;
  children: React.ReactNode;
}

export const Stack: React.FC<StackProps> = ({
  direction = 'col',
  className,
  children,
  ...props
}) => {
  const directionClasses: Record<Direction, string> = {
    col: 'flex-col',
    row: 'flex-row',
    'col-reverse': 'flex-col-reverse',
    'row-reverse': 'flex-row-reverse',
  };

  return (
    <div
      className={twMerge(
        'flex',
        directionClasses[direction],
        className
      )}
      {...props}
    >
      {children}
    </div>
  );
};